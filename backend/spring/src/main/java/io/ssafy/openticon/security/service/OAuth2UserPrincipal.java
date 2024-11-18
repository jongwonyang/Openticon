package io.ssafy.openticon.security.service;

import io.ssafy.openticon.security.member.OAuth2MemberInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class OAuth2UserPrincipal implements OAuth2User, UserDetails {

    private final OAuth2MemberInfo memberInfo;

    public OAuth2UserPrincipal(OAuth2MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberInfo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return memberInfo.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return memberInfo.getEmail();
    }

    public OAuth2MemberInfo getMemberInfo() {
        return memberInfo;
    }
}