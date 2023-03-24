package com.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.answer.AnswerForm;
import com.project.entity.Question;
import com.project.question.QuestionForm;
import com.project.repository.QuestionRepository;
import com.project.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {

	private final QuestionService questionService;

	@GetMapping("/question/list")
	 public String list(Model model) {
	 List<Question> questionList = this.questionService.getList();
	 model.addAttribute("questionList", questionList);
	 return "question_list";
 }
	
	@GetMapping(value = "/question/detail/{id}")
	public String detail(Model model, @PathVariable("id") Long id, AnswerForm answerForm) {
		
		Question question = this.questionService.getQustion(id);
		model.addAttribute("question", question);
		
		return "question_detail";
	}
	
	@GetMapping("/question/create")
	public String questionCreate(QuestionForm questionForm) {
		
		return "question_form";
	}
	
	@PostMapping("/question/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		
	if(bindingResult.hasErrors()) {
		
		return "question_form";
	}
	
		this.questionService.create(questionForm.getTitle(), questionForm.getContent());
		return "redirect:/question/list";
	}
	
	
	
	
	
	
}
