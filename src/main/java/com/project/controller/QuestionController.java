package com.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.entity.Question;
import com.project.repository.QuestionRepository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {

	private final QuestionRepository questionRepository;

	@GetMapping("/question/list")
	 public String list(Model model) {
	 List<Question> questionList = this.questionRepository.findAll();
	 model.addAttribute("questionList", questionList);
	 return "question_list";
	 }

}
