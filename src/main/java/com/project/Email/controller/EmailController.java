package com.project.Email.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Email.dto.MailDto;
import com.project.Email.service.EmailService;
import com.project.service.MemberService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmailController {
	private MemberService ms;
	private EmailService es;
	
	@Transactional
	@PostMapping
	public String sendEmail(@RequestParam("memberEmail") String memberEmail){
        // 임시 비밀번호 생성 & 임시비밀번호로 변경 & 메일 내용 담기
        MailDto dto = es.createMailAndChangePassword(memberEmail);
        // 메일 보내기
        es.mailSend(dto);

        return "members/login";
    }


}
