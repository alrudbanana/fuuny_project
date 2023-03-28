package com.project.controller;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.KakaoProfile;
import com.project.model.User;
import com.project.service.UserService;

import jakarta.servlet.http.HttpSession;

//카카오 리다이렉트 컨트롤러 제발 ㅜㅜ -> 안됨
@RequiredArgsConstructor
@RestController
public class KakaoController {
    private final UserService userService;
    private final HttpSession httpSession;
    @GetMapping("/oauth/kakao?code=")
    public ResponseEntity<?> redirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}