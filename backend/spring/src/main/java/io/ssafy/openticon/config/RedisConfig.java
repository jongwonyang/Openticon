package io.ssafy.openticon.config;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.RedisEmoticonPackEntity;
import io.ssafy.openticon.entity.RedisViewEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory("redis", 6379);
    }

    @Bean
    public RedisTemplate<String, RedisEmoticonPackEntity> redisEmoticonPackTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisEmoticonPackEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key와 Value의 직렬화 방법을 설정합니다.
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, RedisViewEntity> redisMemberViewTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisViewEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key와 Value의 직렬화 방법을 설정합니다.
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

}