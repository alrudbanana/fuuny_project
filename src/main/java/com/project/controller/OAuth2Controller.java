package com.project.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.Session;
import com.project.dto.MemberDto;
import com.project.entity.CustomOAuth2User;
import com.project.entity.Member;
import com.project.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {
	private final MemberService memberService;
	
	private static final String authorizationRequestBaseUri = "oauth2/authorization";

	Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

	private final ClientRegistrationRepository clientRegistrationRepository;

	@SuppressWarnings("unchecked")
	@GetMapping("/login")
	public String getLoginPage(Model model) throws Exception {
		Iterable<ClientRegistration> clientRegistrations = null;
		ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
		if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
		}
		System.out.println((Iterable<ClientRegistration>) clientRegistrationRepository);
		assert clientRegistrations != null;
		clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
				authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
		model.addAttribute("urls", oauth2AuthenticationUrls);
			
		return "login";
	}
	
	
	@GetMapping(value = "/login/{oauth2}")
	public String login(@PathVariable String oauth2, HttpServletResponse httpServletResponse) {
		
		System.out.println("로그인 호출");
		System.out.println("auth2 출력됨 : ==> " + oauth2);
		
		System.out.println(httpServletResponse);
		
		
		return "redirect:/oauth2/authorization/" + oauth2;
	}
	
	@GetMapping("/social/login")
	public String SocialLogin(Model model) {
		 Member member = new Member();
		    member.setMemName("홍길동"); // 값을 설정해줌

		    model.addAttribute("member", member); // 모델에 값 추가
		return "socailMain";
	}

	
	@RequestMapping("/accessDenied")
	public String accessDenied() {
		return "accessDenied";
	}
}
