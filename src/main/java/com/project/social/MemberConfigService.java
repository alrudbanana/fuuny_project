package com.project.social;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.entity.Member;
import com.project.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class MemberConfigService implements UserDetailsService {
	
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Member> _member = this.memberRepository.findByEmail(username);
		
	      if(_member.isEmpty()) {
	         throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
	      }
	      Member member = _member.get();
	      
	      List<GrantedAuthority> authorities = new ArrayList<>();
	      
	      authorities.add(new SimpleGrantedAuthority(member.getRole().getValue()));
	      
	      return new User(member.getEmail(), member.getMemPass(),authorities);
	      
	   }



}
