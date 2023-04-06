package com.project.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.entity.Member;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {


		Optional<Member> findByEmail(String email); 	

		
		//2023.03.25 유저관리 페이징 처리
		Page<Member> findAll(Pageable pageable);
	

		Optional<Member> findByMemName(String memname);

	
		//3.30 멤버 프로필 관련(옵셔널x)
		Member findByIdx(Long idx);
		
		@Modifying
	    @Query("UPDATE Member SET token = null WHERE email = :email")
	    void removeTokenByEmail(String email);

}
