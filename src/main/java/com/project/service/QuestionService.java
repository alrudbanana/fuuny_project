package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.DataNotFoundException;
import com.project.entity.Question;
import com.project.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	public List<Question> getList(){
		
		return this.questionRepository.findAll();
	}
	
	public Question getQustion(Long id) {
		
		Optional<Question> question = this.questionRepository.findById(id);
		
		if(question.isPresent()) {
			
			return question.get();
			
		}else {
			
			throw new DataNotFoundException("question not found");
		}
	}

}
