package io.ssafy.openticon.scheduler;

import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.Set;

@Component
public class RedisViewScheduler {
    private final RedisTemplate<String, EmoticonPackResponseDto> redisEmoticonPackTemplate;
    private final PackRepository packRepository;
    public RedisViewScheduler(
            @Qualifier("redisEmoticonPackTemplate") RedisTemplate<String, EmoticonPackResponseDto> redisEmoticonPackTemplate,
            PackRepository packRepository){
        this.redisEmoticonPackTemplate = redisEmoticonPackTemplate;
        this.packRepository = packRepository;
    }

    @Scheduled(cron = "0 */10 * * * *") // 매 0, 10, 20, 30, 40, 50분마다 조회수를 데이터 베이스에 저장
    @Transactional
    public void updateViewCount() {
        Set<String> keys = redisEmoticonPackTemplate.keys("emoticon_pack:*");
        if(keys != null){
            System.out.println("10분 스케줄러 동작 중! redisKeys: " + keys.size());
            for(String redisKey : keys){
                EmoticonPackResponseDto responseDto = (EmoticonPackResponseDto) redisEmoticonPackTemplate.opsForValue().get(redisKey);
                if(responseDto != null){
                    Optional<EmoticonPackEntity> emoticonPackEntity = packRepository.findById(responseDto.getId());
                    if(emoticonPackEntity.isEmpty()){
                        throw new OpenticonException(ErrorCode.EMOTICON_PACK_EMPTY);
                    }
                    emoticonPackEntity.get().setView(responseDto.getView());
                    packRepository.save(emoticonPackEntity.get());
                    redisEmoticonPackTemplate.delete(redisKey);
                }
            }
        }else{
            System.out.println("1분 스케줄러 동작 중! redisKeys: 0");
        }
    }
}
