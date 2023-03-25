package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.answer.AnswerForm;
import com.project.entity.Question;
import com.project.service.AnswerService;
import com.project.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	
	private final QuestionService questionService;
	
	private final AnswerService answerService;
	
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Long id, @Valid AnswerForm answerForm, BindingResult bindingResult) {
		
		Question question = this.questionService.getQustion(id);
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			
			return "question_detail";
		}
		this.answerService.create(question, answerForm.getContent());
		
		return String.format("redirect:/question/detail/%s", id);
	}

}
