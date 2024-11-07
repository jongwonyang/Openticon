package io.ssafy.openticon.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisEmoticonPackEntity {
    @Id
    private String id; // Composite key of email and emoticon pack ID
    private Long emoticonPackId;
    private Long view;
}
