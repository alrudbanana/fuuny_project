package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
