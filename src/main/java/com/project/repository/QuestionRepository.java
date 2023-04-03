package com.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	Question findBytitle(String title);
	Question findBytitleAndContent(String title, String content);
	List<Question> findBytitleLike(String title);
	Page<Question> findAll(Pageable pageable);
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	Page<Question> findByMemberIdx(Long Idx , Pageable pageable);
}
