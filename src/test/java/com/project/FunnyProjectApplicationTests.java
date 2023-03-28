package com.project;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
	

	
	@Autowired
	private AdminService adminService;

	@Test
	void testAdmin() {
		for (int i = 1; i <= 300; i++) {
            String title = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.adminService.writeNotice(title, content);
        }

	}


}
