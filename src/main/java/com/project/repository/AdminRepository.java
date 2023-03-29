package com.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Notice;

public interface AdminRepository extends JpaRepository<Notice, Integer> {
	
	//페이징 처리
	Page<Notice> findAll(Pageable pageable);

}
