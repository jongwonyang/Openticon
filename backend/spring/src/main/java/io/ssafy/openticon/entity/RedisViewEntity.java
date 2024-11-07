package io.ssafy.openticon.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "redisView", timeToLive = 3600L) // TTL set to 1 hour (3600 seconds)
public class RedisViewEntity {

    @Id
    private String id; // Composite key of email and emoticon pack ID

    private String email;

    private Long emoticonPackId;

    private String requestIp;
}
