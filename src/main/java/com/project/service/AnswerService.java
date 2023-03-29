package com.project.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.entity.Answer;
import com.project.entity.Member;
import com.project.entity.Question;
import com.project.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	public void create(Question question, String content) {
		
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setRegTime(LocalDateTime.now());
		answer.setQuestion(question);
		this.answerRepository.save(answer);
	}
	
	public Answer create(Question question, String content, Member member ) {
		
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setRegTime(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setMember(member);
		this.answerRepository.save(answer);
		return answer;
	}

}
