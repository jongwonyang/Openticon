package io.ssafy.openticon.scheduler;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.RedisEmoticonPackEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.PackRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Component
public class RedisViewScheduler {
    private final RedisTemplate<String, RedisEmoticonPackEntity> redisEmoticonPackTemplate;
    private final PackRepository packRepository;
    public RedisViewScheduler(
            @Qualifier("redisEmoticonPackTemplate") RedisTemplate<String, RedisEmoticonPackEntity> redisEmoticonPackTemplate,
            PackRepository packRepository){
        this.redisEmoticonPackTemplate = redisEmoticonPackTemplate;
        this.packRepository = packRepository;
    }

    @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
    @Transactional
    public void updateViewCount() {
        Set<String> keys = redisEmoticonPackTemplate.keys("emoticon_pack:*");
        if(keys != null){
            System.out.println("1분 스케줄러 동작 중! redisKeys: " + keys.size());
            for(String redisKey : keys){
                RedisEmoticonPackEntity redisEmoticonPack = (RedisEmoticonPackEntity) redisEmoticonPackTemplate.opsForValue().get(redisKey);
                if(redisEmoticonPack != null){
                    Optional<EmoticonPackEntity> emoticonPackEntity = packRepository.findById(redisEmoticonPack.getEmoticonPackId());
                    if(emoticonPackEntity.isEmpty()){
                        throw new OpenticonException(ErrorCode.EMOTICON_PACK_EMPTY);
                    }
                    emoticonPackEntity.get().setView(redisEmoticonPack.getView());
                    packRepository.save(emoticonPackEntity.get());
                    redisEmoticonPackTemplate.delete(redisKey);
                }
            }
        }else{
            System.out.println("1분 스케줄러 동작 중! redisKeys: 0");
        }
    }
}
