package io.ssafy.openticon.config;


import io.ssafy.openticon.utils.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 모든 요청에 대해 인터셉터가 작동하도록 설정
        registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
    }
}
