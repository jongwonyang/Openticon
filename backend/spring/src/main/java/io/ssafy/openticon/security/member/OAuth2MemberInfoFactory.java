package io.ssafy.openticon.security.member;

import io.ssafy.openticon.security.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2MemberInfoFactory {

    public static OAuth2MemberInfo getOAuth2UserInfo(String registrationId,
                                                   String accessToken,
                                                   Map<String, Object> attributes) {
        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2MemberInfo(accessToken, attributes);
        } else if (OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) {
            return new NaverOAuth2MemberInfo(accessToken, attributes);
        } else if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            return new KakaoOAuth2MemberInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}
