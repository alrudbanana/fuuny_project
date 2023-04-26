package com.project.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.project.entity.Member;


public class CustomOAuth2User implements OAuth2User {
	
	private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String name;
    private final Member member;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String name, Member member) {
        this.authorities = authorities;
        this.attributes = attributes;
        this.name = name;
        this.member = member;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    public Member getMember() {
        return member;
    }
}
