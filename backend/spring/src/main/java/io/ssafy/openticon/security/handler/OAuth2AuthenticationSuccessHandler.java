package io.ssafy.openticon.security.handler;

import io.ssafy.openticon.security.HttpCookieOAuth2AuthorizationRequestRepository;
import io.ssafy.openticon.security.jwt.TokenProvider;
import io.ssafy.openticon.security.service.OAuth2UserPrincipal;
import io.ssafy.openticon.security.member.OAuth2Provider;
import io.ssafy.openticon.security.member.OAuth2MemberUnlinkManager;
import io.ssafy.openticon.security.util.CookieUtils;
import io.ssafy.openticon.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import io.ssafy.openticon.entity.MemberEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static io.ssafy.openticon.security.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static io.ssafy.openticon.security.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2MemberUnlinkManager oAuth2MemberUnlinkManager;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");
        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        String email = principal.getMemberInfo().getEmail();
        // DB에서 사용자 존재 여부 확인 (예: userService.findByEmail(email))
        boolean userExists = memberRepository.existsMemberByEmail(email);
        if ("login".equalsIgnoreCase(mode)) {
            String providerName = principal.getMemberInfo().getProvider().name(); // Kakao, Naver, Google 중 하나
            String nickname = Optional.ofNullable(principal.getMemberInfo().getNickname())
                    .orElse(principal.getMemberInfo().getName()) + "_" + providerName;
            System.out.println(principal.getMemberInfo().getName()+" "+principal.getMemberInfo().getNickname());
            if(!userExists){

                MemberEntity member = MemberEntity.builder()
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .nickname(nickname)
                        .email(principal.getMemberInfo().getEmail())
                        .profile_image(Optional.ofNullable(principal.getMemberInfo().getProfileImageUrl()).orElse(""))  // null이면 빈 문자열
                        .build();
                MemberEntity member1 = memberRepository.save(member);
                member1.setNickname("새로운 사용자" + member1.getId());
                memberRepository.save(member1);
                System.out.println(member1);
            }
            log.info("email={}, name={}, nickname={}, accessToken={}, profile_image={}",
                    principal.getMemberInfo().getEmail(),
                    principal.getMemberInfo().getName(),
                    principal.getMemberInfo().getNickname(),
                    principal.getMemberInfo().getAccessToken(),
                    principal.getMemberInfo().getProfileImageUrl()
            );

            String accessToken = tokenProvider.createToken(authentication);
            String refreshToken = "test_refresh_token";

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("access_token", accessToken)
                    .build().toUriString();

        } else if ("unlink".equalsIgnoreCase(mode)) {

            String accessToken = principal.getMemberInfo().getAccessToken();
            OAuth2Provider provider = principal.getMemberInfo().getProvider();
            MemberEntity member = (MemberEntity) memberRepository.findMemberByEmail(principal.getMemberInfo().getEmail())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            memberRepository.delete(member);
            oAuth2MemberUnlinkManager.unlink(provider, accessToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}