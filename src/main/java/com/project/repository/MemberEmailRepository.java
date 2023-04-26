package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Member;

public interface MemberEmailRepository extends JpaRepository<Member, Long> {
	
	Member findByEmail(String email);
}
