package com.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import com.project.DataNotFoundException;
import com.project.entity.Member;
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
	
	public void create(String title, String content, Member member) {
		Question q = new Question();
		q.setTitle(title);
		q.setContent(content);
		q.setRegTime(LocalDateTime.now());
		this.questionRepository.save(q);
	}
	
	public Page<Question> getList(int page) {
		 List<Sort.Order> sorts = new ArrayList<>();
		 sorts.add(Sort.Order.desc("regTime"));
		 Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		 return this.questionRepository.findAll(pageable);
		 }
}
