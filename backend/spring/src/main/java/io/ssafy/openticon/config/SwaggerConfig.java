package io.ssafy.openticon.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@OpenAPIDefinition(
        servers = {
                @Server(url = "https://apitest.openticon.store/api/v1", description = "Public Openticon https 서버"),
                @Server(url = "http://127.0.0.1:8080/api/v1", description = "Local Openticon http 서버"),
                @Server(url = "http://localhost:8080/api/v1", description = "Local Openticon http 서버")
        }
)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Openticon Swagger")
                .description("오픈티콘 프로젝트 Swagger")
                .version("1.0.0");
    }
    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("유저 관련")
                .pathsToMatch("/member/**")
                .build();
    }
    @Bean
    public GroupedOpenApi healthcheckApi() {
        return GroupedOpenApi.builder()
                .group("헬스 체크")
                .pathsToMatch("/health/**")
                .build();
    }

    @Bean
    public GroupedOpenApi emoticonPackApi() {
        return GroupedOpenApi.builder()
                .group("이모티콘 팩 관련 기능")
                .pathsToMatch("/emoticonpacks/**")
                .build();
    }

    @Bean
    public GroupedOpenApi pointApi() {
        return GroupedOpenApi.builder()
                .group("포인트 관련 기능")
                .pathsToMatch("/points/**")
                .build();
    }



}
