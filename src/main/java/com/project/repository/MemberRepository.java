package com.project.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {
		Optional<Member> findByEmail(String email);
		
		//2023.03.25 유저관리 페이징 처리
		Page<Member> findAll(Pageable pageable);
		
		
}
