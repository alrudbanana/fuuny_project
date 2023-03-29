package com.project.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.model.KakaoProfile;
import com.project.model.User;
import com.project.service.UserService;

import jakarta.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class KakaoController {
    private final UserService userService;
    private final HttpSession httpSession;
    @GetMapping("/")
    public String index(Model model, User user) {
        // .............
        // 사용자 정보: 위의 @LoginUser 어노테이션으로 대체
        // SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if(user != null) {
            model.addAttribute("userName", user.getKakaoNickname());
            model.addAttribute("userImg", user.getKakaoProfileImg());
        }
        return "index";
    }
}