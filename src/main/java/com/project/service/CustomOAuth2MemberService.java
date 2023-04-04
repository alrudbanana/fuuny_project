package com.project.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.constant.Role;
import com.project.constant.Social;
import com.project.entity.Member;
import com.project.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	Member member;
	@Autowired
	HttpSession httpSession;
	
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
	    OAuth2User oAuth2User = delegate.loadUser(userRequest);
	    System.out.println(1);

	    String registration = userRequest.getClientRegistration().getRegistrationId();
	    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
	    
	    String email;
	    String name;
	    String token;

	    Map<String, Object>response = oAuth2User.getAttributes();

	    System.out.println(2);
	    if(registration.equals("naver")) {
	        Map<String, Object> hash = (Map<String, Object>)response.get("response");
	        email = (String) hash.get("email");
	        System.out.println(email);
	        name = (String) hash.get("name");
	        System.out.println(name);
	        token =(String) hash.toString();
	    } else if(registration.equals("google")) {
	        email = (String) response.get("email");
	        name = (String) response.get("name");
	       	token = (String) response.get("sub");
	       	System.out.println(3);
	    } else {
	        throw new OAuth2AuthenticationException("허용되지 않는 인증입니다.");
	    }
	    
	    Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, AuthorityUtils.createAuthorityList(member.getRole().toString()));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	    Member member = null;
	    Optional<Member> optionalMember = memberRepository.findByEmail(email);
	    if(optionalMember.isPresent()) {
	        member = optionalMember.get();
	        member.setToken(token);
	        this.memberRepository.save(member);
	    } else {
	        member = new Member();
	        member.setEmail(email);
	        member.setMemName(name);
	        member.setToken(token);
	        member.setRole(Role.USER);
	        member.setSocial(registration.equals("naver") ? Social.NAVER : Social.GOOGLE);
	        this.memberRepository.save(member);
	    }
	    httpSession.setAttribute("member", member);

	    return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
	            oAuth2User.getAttributes(),
	            userNameAttributeName);
	}
	
	
}
