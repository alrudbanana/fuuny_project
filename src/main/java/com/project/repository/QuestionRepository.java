package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	Question findBytitle(String title);
	Question findBytitleAndContent(String title, String content);
	List<Question> findBytitleLike(String title);

}
