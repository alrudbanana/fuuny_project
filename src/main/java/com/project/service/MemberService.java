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


import org.springframework.aot.hint.MemberCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.DataNotFoundException;

import com.project.Role;
import com.project.dto.MemberDto;
import com.project.dto.MemberFormDto;
import com.project.dto.MemberUpdateDto;
import com.project.dto.pwdDto;
import com.project.entity.Member;
import com.project.repository.MemberRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
		Optional<Member> op = this.memberRepository.findById(idx) ;
		if ( op.isPresent()) {		 
			return op.get();	
		}else {
			throw new DataNotFoundException("요청한 파일을 찾지 못했습니다.") ;
		}
	
	}
	//정보수정
	 public void modify( Member member , MemberDto memberDto , String email , String memName, String memPhone, String zipcode, String streeAdr, String detailAdr) {
		 	 
		 member.setEmail(email);
		 member.setMemPass(this.passwordEncoder.encode(member.getMemPass()));
		 member.setMemName(memName);
		 member.setMemPhone(memPhone);
		 member.setZipcode(zipcode);
		 member.setStreetAdr(streeAdr);
		 member.setDetailAdr(detailAdr);
		 this.memberRepository.save(member);
		 
	 }
	 
	 
	 //2.비밀번호 수정
	 public void modifyPw(Member member) throws Exception {
		 
		 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
		 String securePw = encoder.encode(member.getMemPass());
		 member.setMemPass(securePw);
		 
		 this.memberRepository.save(member);
	 }
	 
	 
	 public void delete(Member member) {
		 this.memberRepository.delete(member);
		 }
	 
	 
	//로그인
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
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

		return new User(member.getEmail(),member.getMemPass(), authorities);
    }  
    


    
    
    public Member getMember1(String email) {
    	Optional<Member> member = this.memberRepository.findByEmail(email);
    	return member.get();
    }



  //사용자 조회 
    public Member getMember(String memName) {
    	
    	  	
    	 Optional<Member> member = this.memberRepository.findByEmail(memName);
    	 if (member.isPresent()) {
    		
    		 return member.get();
    	 } else {
    		
    		 throw new DataNotFoundException("siteuser not found ");
    	 }
    }

    public boolean checkPassword(Long member_id, String checkPassword) {
        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        String realPassword = member.getMemPass();
        boolean matches = passwordEncoder.matches(checkPassword, realPassword);
        return matches;
    }
    
    //비밀번호 수정
	 public void modifyPw(pwdDto pwdDto , Member member) {
		 
		 member.setMemPass(this.passwordEncoder.encode(pwdDto.getNewPwd()));
		 
		 this.memberRepository.save(member);
	 }
	 
	 private final MemberUpdateDto memberUpdateDto;

	 //임시비밀번호로 비밀번호 수정 
	 public void updatePassword(Long memberId, String memberPassword) {
		    Member member = memberRepository.findById(memberId).orElseThrow();
		    member.setMemPass(memberPassword);
		    memberRepository.save(member);
		}
	}

