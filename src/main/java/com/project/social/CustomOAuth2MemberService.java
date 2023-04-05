package com.project.social;

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

import com.project.constant.Role;
import com.project.constant.Social;
import com.project.dto.MemberDto;
import com.project.entity.Member;
import com.project.repository.MemberRepository;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	
	private final MemberRepository memberRepository;
	private final HttpSession httpSession;
	
		@Override
		public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
			OAuth2UserService delegate = new DefaultOAuth2UserService();
		    OAuth2User oAuth2User = delegate.loadUser(userRequest);

		    String registration = userRequest.getClientRegistration().getRegistrationId();
		   
		    
		    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		    
		    String email;
		    String name;
		    String token;

		    Map<String, Object>response = oAuth2User.getAttributes();

		    if(registration.equals("naver")) {
		        Map<String, Object> hash = (Map<String, Object>)response.get("response");
		        email = (String) hash.get("email");
		        name = (String) hash.get("name");
		        token =(String) hash.toString();
		    } else if(registration.equals("google")) {
		        email = (String) response.get("email");
		        name = (String) response.get("name");
		       	token = (String) response.get("sub");
		    } else {
		        throw new OAuth2AuthenticationException("허용되지 않는 인증입니다.");
		    }
		    
		    Member member = null;
		    
		    Optional<Member> optionalMember = memberRepository.findByEmail(email);
		    if(optionalMember.isPresent()) {
		        member = optionalMember.get();
		        member.setToken(token);
		        this.memberRepository.save(member);
		    } else {
		    	member = new Member();
	            member.setMemName(name);
	            member.setEmail(email);
	            member.setToken(token);
	            member.setRole(Role.USER);
		        if(registration.equals("naver")) {
		            member.setSocial(Social.NAVER);
		            this.memberRepository.save(member);
		        } else if(registration.equals("google")) {
		            member.setSocial(Social.GOOGLE);
		            this.memberRepository.save(member);
		        }
		    }
		    httpSession.setAttribute("member", new MemberDto(member));
		    
		    
		    return new DefaultOAuth2User(Collections.singleton
		    		(new SimpleGrantedAuthority(member.getRole().toString())),
		            oAuth2User.getAttributes(),
		            userNameAttributeName);
		}
		 // 소셜 로그인 시, 소셜 로그인에 등록된 실명과 이메일로 가입한 회원이 존재하지 않을 경우 null
	    private Member check(MemberDto memberDto) {
	    	Member member = memberRepository.findByEmail(memberDto.getEmail())
	                .orElseThrow(() -> new OAuth2AuthenticationException("가입된 회원이 아닙니다."));

	        return member;
	    }
	}
