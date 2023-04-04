package com.project.social;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
	Optional<SocialMember> findByEmail(String email);
}
