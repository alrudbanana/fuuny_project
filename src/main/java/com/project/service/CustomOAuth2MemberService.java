package com.project.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.project.constant.Role;
import com.project.constant.Social;
import com.project.dto.MemberDto;
import com.project.entity.CustomOAuth2User;
import com.project.entity.Member;
import com.project.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Service
@AllArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberRepository memberRepository;
	private final HttpSession httpSession;
	private final HttpServletRequest request;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	    OAuth2UserService delegate = new DefaultOAuth2UserService();
	    OAuth2User oAuth2User = delegate.loadUser(userRequest);

	    String registrationId = userRequest.getClientRegistration().getRegistrationId();
	    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
	            .getUserInfoEndpoint().getUserNameAttributeName();

	    String email = null;
	    String name = null;
	    String token = null;
	    String memName = "소셜로그인";

	    Map<String, Object> attributes = oAuth2User.getAttributes();

	    if(registrationId.equals("naver")) {
	        Map<String, Object> hash = (Map<String, Object>)attributes.get("response");
	        email = (String) hash.get("email");
	        name = (String) hash.get("name");
	        token = (String) hash.toString();
	    } else if(registrationId.equals("google")) {
	        email = (String) attributes.get("email");
	        name = (String) attributes.get("name");
	        token = (String) attributes.get("sub");
	    }

	    Member member;
	    Optional<Member> optionalMember = memberRepository.findByEmail(email);

	    if(optionalMember.isPresent()) {
	        member = optionalMember.get();
	        member.setToken(token);
	        member.setMemName(memName);
	        memberRepository.save(member);
	    } else {
	        member = new Member();
	        member.setEmail(email);
	        member.setToken(token);
	        member.setMemName(memName);
	        member.setRole(Role.USER);

	        if(registrationId.equals("naver")) {
	            member.setSocial(Social.NAVER);
	            memberRepository.save(member);
	        } else if(registrationId.equals("google")) {
	            member.setSocial(Social.GOOGLE);
	            memberRepository.save(member);
	        }
	    }
	    
	    if (RequestContextHolder.getRequestAttributes() != null) {
	        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	        HttpSession session = request.getSession(true);
	        session.setAttribute("member", member);
	        System.out.println(member);
	    }

	    return new CustomOAuth2User(
	            Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
	            attributes,
	            userNameAttributeName,
	            member
	    );
	}

		/*
		private Member check(MemberDto memberDto, Optional<Member> optionalMember) {
		    Member member = optionalMember.orElseGet(() -> {
		        Member newMember = new Member();
		        newMember.setEmail(memberDto.getEmail());
		        newMember.setMemName(memberDto.getMemName());
		        newMember.setRole(Role.USER);
		        return memberRepository.save(newMember);
		    });

		    return member;
		}
		*/
	}
