package com.project.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.DataNotFoundException;

import com.project.Role;

import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	
	//2023.03.25 유저생성시 권한, 생성날짜 추가생성
	public void saveMember(MemberFormDto memberFromDto) {
		Member member = new Member();
		member.setEmail(memberFromDto.getEmail());
		member.setMemPass(this.passwordEncoder.encode(memberFromDto.getMemPass()));
		member.setMemName(memberFromDto.getMemName());
		member.setMemPhone(memberFromDto.getMemPhone());
		member.setZipcode(memberFromDto.getZipcode());		
		member.setStreetAdr(memberFromDto.getStreetAdr());
		member.setDetailAdr(memberFromDto.getDetailAdr());

		member.setRole(memberFromDto.getRole());
		member.setRegTime(LocalDateTime.now());

		this.memberRepository.save(member);
		
	}
	
	public Member getMember(Long idx) {
		
		//select * from question where id = ? 
		Optional<Member> op = this.memberRepository.findById(idx) ;
		if ( op.isPresent()) {		// op에 값이 존재하는 경우 
			return op.get();	// Question 객체를 리턴
		}else {
			// 사용자 정의 예외 : 
			// throw : 예외를 강제로 발생
			// throws : 예외를 요청한 곳에서 처리하도록 미루는 것
			throw new DataNotFoundException("요청한 파일을 찾지 못했습니다.") ;
		}
	
	}
	
	 public void modify(Member member , String email , String memPass, String memName, String memPhone, String zipcode, String streeAdr, String detailAdr) {
		 member.setEmail(email);
		 member.setMemPass(this.passwordEncoder.encode(member.getMemPass()));
		 member.setMemName(memName);
		 member.setMemPhone(memPhone);
		 member.setZipcode(zipcode);
		 member.setStreetAdr(streeAdr);
		 member.setDetailAdr(detailAdr);
		 this.memberRepository.save(member);
		 }
	 
	//로그인
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	System.out.println(email); //콘솔에 정보를 출력함 : 개발 완료 시는 제거함 
		
		Optional<Member> _Member=this.memberRepository.findByEmail(email);
		
		if(_Member.isEmpty()) {
			
			System.out.println("비어있음");
			throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
			
		}
		
		Member member = _Member.get();
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		System.out.println(member.getRole());
		
		//2023.03.27 관리자 로그인시 관리자 버튼 생성 / 기반 마련
		if("ADMIN".equals(member.getRole().toString())) {
			authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
			System.out.println("Admin Role 호출됨");
			System.out.println(Role.ADMIN.getValue());
		}else if("SELLER".equals(member.getRole().toString())){
			authorities.add(new SimpleGrantedAuthority(Role.SELLER.getValue()));
			System.out.println("SELLER Role 호출됨");
			System.out.println(Role.ADMIN.getValue());
		}else if("USER".equals(member.getRole().toString())){
			authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
			System.out.println("USER Role 호출됨");
			System.out.println(Role.ADMIN.getValue());
		}

		return new User(member.getEmail(),member.getMemPass(),authorities);
    }  
    
  

}