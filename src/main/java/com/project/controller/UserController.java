package com.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@RestController
public class UserController {
	
    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/oauth/kakao")
    public String getLogin(@RequestParam("code") String code, HttpSession session, HttpServletResponse response) {
        OauthToken oauthToken = userService.getAccessToken(code);
        User user = userService.saveUser(oauthToken.getAccess_token());
        session.setAttribute("user", user); // 로그인한 유저를 세션에 저장
        try {
            response.sendRedirect("/"); // home으로 redirect
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
