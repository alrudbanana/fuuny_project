package com.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.answer.AnswerForm;
import com.project.entity.Member;
import com.project.entity.Question;
import com.project.question.QuestionForm;
import com.project.repository.QuestionRepository;
import com.project.service.MemberService;
import com.project.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {

	private final QuestionService questionService;
	private final MemberService memberService;

	@GetMapping("/question/list")
	 public String list(Model model, @RequestParam(value = "page", defaultValue ="0" )int page) {
	 Page<Question> paging = this.questionService.getList(page);
	 model.addAttribute("paging", paging);
	 return "question_list";
 }
	
	@GetMapping(value = "/question/detail/{id}")
	public String detail(Model model, @PathVariable("id") Long id, AnswerForm answerForm) {
		
		Question question = this.questionService.getQustion(id);
		model.addAttribute("question", question);
		
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/question/create")
	public String questionCreate(QuestionForm questionForm) {
		
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/question/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		
	if(bindingResult.hasErrors()) {
		
		return "question_form";
	}
	
		Member member = this.memberService.getUser(principal.getName());
		this.questionService.create(questionForm.getTitle(), questionForm.getContent(), member);
		return "redirect:/question/list";
	}
	

		
	
}
