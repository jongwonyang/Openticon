package io.ssafy.openticon.config;

import io.ssafy.openticon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RedisDataInitializer {

    @Autowired
    private TagService tagService;

    @Bean
    public ApplicationRunner initializeRedisData() {
        return args -> {
            List<String> initialTags = tagService.initTags();
            tagService.updateTags(initialTags);
            System.out.println("Redis에 초기 태그가 저장되었습니다: " + initialTags);
        };
    }
}
