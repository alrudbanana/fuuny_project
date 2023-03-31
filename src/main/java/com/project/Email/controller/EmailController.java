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
	

}
