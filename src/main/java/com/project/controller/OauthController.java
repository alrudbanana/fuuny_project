package com.project.controller;

import java.net.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RestController
public class OauthController {
	
	@GetMapping("/oauth/kakao")
	@ResponseBody
	public void kakaoCalllback(@RequestParam String code) {
		
		//데이터를 리턴해주는 컨트롤러 함
		//post방식으로 key=value 데이터를 요청 (카카오쪽으로)
		//api는 무조건 get방식 
		System.out.println(code);
	}
}
