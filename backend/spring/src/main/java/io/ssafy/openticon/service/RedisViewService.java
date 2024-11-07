package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.RedisEmoticonPackEntity;
import io.ssafy.openticon.entity.RedisViewEntity;
import io.ssafy.openticon.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisViewService {
    private final RedisTemplate<String, RedisEmoticonPackEntity> redisEmoticonPackTemplate;
    private final RedisTemplate<String, RedisViewEntity> redisMemberViewTemplate;
    private final MemberRepository memberRepository;
    private final long TTL = 5; // TTL
    private final TimeUnit TIME_SET = TimeUnit.SECONDS; // 적용 시간

    public RedisViewService(
            @Qualifier("redisEmoticonPackTemplate") RedisTemplate<String, RedisEmoticonPackEntity> redisEmoticonPackTemplate,
            @Qualifier("redisMemberViewTemplate") RedisTemplate<String, RedisViewEntity> redisMemberViewTemplate,
            MemberRepository memberRepository
    ){
        this.redisEmoticonPackTemplate = redisEmoticonPackTemplate;
        this.redisMemberViewTemplate = redisMemberViewTemplate;
        this.memberRepository = memberRepository;
    }

    public void emoticonPackIncrementView(EmoticonPackEntity emoticonPack){
        String redisKey = "emoticon_pack:"+emoticonPack.getId();

        RedisEmoticonPackEntity redisEmoticonPack = (RedisEmoticonPackEntity) redisEmoticonPackTemplate.opsForValue().get(redisKey);

        // redis에 이모티콘 팩이 없는 경우
        if(redisEmoticonPack == null){
            redisEmoticonPack = RedisEmoticonPackEntity.builder()
                    .id(redisKey)
                    .emoticonPackId(emoticonPack.getId())
                    .view(emoticonPack.getView())
                    .build();
        }

        // 1 증가
        redisEmoticonPack.setView(redisEmoticonPack.getView() + 1);
        redisEmoticonPackTemplate.opsForValue().set(redisKey, redisEmoticonPack);
    }

    public void incrementView(EmoticonPackEntity emoticonPack, UserDetails userDetails, String requestIp) {
        String memberEmail = "Anonymous";
        if(userDetails != null){
            Optional<MemberEntity> memberEntity = memberRepository.findMemberByEmail(userDetails.getUsername());
            if(memberEntity.isPresent()) memberEmail = memberEntity.get().getEmail();
        }
        String compositeKey = requestIp + ":" + memberEmail + ":" + emoticonPack.getId(); // Redis에서 사용할 Key

        RedisViewEntity viewEntity = RedisViewEntity.builder()
                .id(compositeKey)
                .email(memberEmail)
                .requestIp(requestIp)
                .emoticonPackId(emoticonPack.getId())
                .build();

        if (Boolean.FALSE.equals(redisMemberViewTemplate.hasKey(compositeKey))) {
            // Redis 만료 시간 설정
            emoticonPackIncrementView(emoticonPack);
            redisMemberViewTemplate.opsForValue().set(compositeKey, viewEntity, TTL, TIME_SET);
        }
    }
}
