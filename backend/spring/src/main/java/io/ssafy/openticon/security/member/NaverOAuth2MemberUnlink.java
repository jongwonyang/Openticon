package io.ssafy.openticon.security.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class NaverOAuth2MemberUnlink implements OAuth2MemberUnlink {

    private static final String URL = "https://nid.naver.com/oauth2.0/token";
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Override
    public void unlink(String accessToken) {

        String url = URL +
                "?service_provider=NAVER" +
                "&grant_type=delete" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&access_token=" + accessToken;

        UnlinkResponse response = restTemplate.getForObject(url, UnlinkResponse.class);

        if (response != null && !"success".equalsIgnoreCase(response.getResult())) {
            throw new RuntimeException("Failed to Naver Unlink");
        }
    }

    @Getter
    @NoArgsConstructor // 기본 생성자 추가
    public static class UnlinkResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("result")
        private String result;
    }
}
