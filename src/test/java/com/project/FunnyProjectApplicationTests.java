package com.project;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.entity.Answer;
import com.project.entity.Question;
import com.project.repository.AnswerRepository;

import com.project.entity.Notice;
import com.project.entity.Question;
import com.project.repository.AdminRepository;

import com.project.repository.QuestionRepository;
import com.project.service.AdminService;

@SpringBootTest
class FunnyProjectApplicationTests {
	
//	@Autowired
//	private QuestionRepository questionRepository;
//	
//	@Test
//	void testJpa() {
//		Question q1 = new Question();
//		q1.setTitle("Test 입니다.");
//		q1.setContent("Test 내용입니다.");
//		q1.setRegTime(LocalDateTime.now());
//		this.questionRepository.save(q1);	// 첫번째 질문 저장
//		
//		Question q2 = new Question();
//		q2.setTitle("Test2 입니다.");
//		q2.setContent("Test2 내용입니다.");
//		q2.setRegTime(LocalDateTime.now());
//		this.questionRepository.save(q2);	// 첫번째 질문 저장
//	}
//
//	@Test
//	void contextLoads() {
//	}
/*	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AnswerRepository answerRepository;
<<<<<<< HEAD
/*	
	@Test

	void testJpa() {
		Question q1 = new Question();
		q1.setTitle("Test 입니다.");
		q1.setContent("Test 내용입니다.");
		q1.setRegTime(LocalDateTime.now());
		this.questionRepository.save(q1);	// 첫번째 질문 저장
		
		Question q2 = new Question();
		q2.setTitle("Test2 입니다.");
		q2.setContent("Test2 내용입니다.");
		q2.setRegTime(LocalDateTime.now());
		this.questionRepository.save(q2);	// 첫번째 질문 저장
		
		Optional<Question> oq = this.questionRepository.findById((long) 1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		
		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);
		a.setRegTime(LocalDateTime.now());
		this.answerRepository.save(a);
		

	void testAdmin() {
		for (int i = 1; i <= 300; i++) {
            String title = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.adminService.writeNotice(title, content);
        }

	}
*/	


	

	
	


}
