package com.project.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.answer.AnswerForm;
import com.project.entity.Member;
import com.project.entity.Question;
import com.project.service.AnswerService;
import com.project.service.MemberService;
import com.project.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	
	

	private final QuestionService questionService;
	
	private final AnswerService answerService;
	
	private final MemberService memberService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Long id, 
		@Valid AnswerForm answerForm, BindingResult bindingResult,Principal principal) {
		
		Question question = this.questionService.getQustion(id);
		
		Member member = this.memberService.getMember1(principal.getName());
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			
			return "question_detail";
		}
		this.answerService.create(question, answerForm.getContent(), member);
		
		return String.format("redirect:/question/detail/%s", id);
	}

}
