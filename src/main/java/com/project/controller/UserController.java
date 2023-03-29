package com.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dto.MemberFormDto;
import com.project.model.KakaoProfile;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.OauthToken;
import com.project.service.UserService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController //(1)
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService; //(2)
    
    // 프론트에서 인가코드 받아오는 url
   @GetMapping("/oauth/kakao") // (3)
   public OauthToken getLogin(@RequestParam("code") String code) { //(4)

       // 넘어온 인가 코드를 통해 access_token 발급 //(5)
       OauthToken oauthToken = userService.getAccessToken(code);
       ///(1)
       //발급 받은 accessToken 으로 카카오 회원 정보 DB 저장
       User user = userService.saveUser(oauthToken.getAccess_token());
       return oauthToken;

   }
   
}
