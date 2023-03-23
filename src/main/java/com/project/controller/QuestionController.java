package com.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.entity.Question;
import com.project.repository.QuestionRepository;
import com.project.service.QuestionService;

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
	public String detail(Model model, @PathVariable("id") Long id) {
		
		Question question = this.questionService.getQustion(id);
		model.addAttribute("question", question);
		
		return "question_detail";
	}
	
	
}
