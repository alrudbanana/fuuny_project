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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.DataNotFoundException;
import com.project.FileUploadUtil;
import com.project.constant.Role;
import com.project.dto.MemberDto;
import com.project.dto.MemberFormDto;
import com.project.dto.MemberUpdateDto;
import com.project.dto.pwdDto;
import com.project.entity.Member;
import com.project.entity.Question;
import com.project.item.service.FileService;
import com.project.repository.MemberRepository;
import com.project.repository.QuestionRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {
	
	//프로필 경로 추가
	@Value("${memberImgLocation}")
	private String memberImgLocation;
	
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final FileService fileService;
	
	//2023.03.25 유저생성시 권한, 생성날짜 추가생성
	public void saveMember(MemberFormDto memberFromDto) {
		
		String uploadDir = "/img/profile/default.png"; //프로필 이미지 디폴트
		
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
		
		member.setImgurl(uploadDir); //프로필 이미지 저장

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
	 public void modify( Member member , MemberDto memberDto) {
	 	 
		 member.setEmail(memberDto.getEmail());
		 member.setMemPass(this.passwordEncoder.encode(memberDto.getMemPass()));
		 member.setMemName(memberDto.getMemName());
		 member.setMemPhone(memberDto.getMemPhone());
		 member.setZipcode(memberDto.getZipcode());
		 member.setStreetAdr(memberDto.getStreetAdr());
		 member.setDetailAdr(memberDto.getDetailAdr());
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
			System.out.println(Role.SELLER.getValue());
		}else if("USER".equals(member.getRole().toString())){
			authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
			System.out.println("USER Role 호출됨");
			System.out.println(Role.USER.getValue());
		}
		//수정 
		return User.builder()
                .username(member.getEmail())
                .password(member.getMemPass())
                .authorities(member.getRole().toString())
                .build();
    }  
    
    public void removeToken(String email) {
        memberRepository.removeTokenByEmail(email);
    }

    
    /*
    public Member getMember1(String email) {
    	Optional<Member> member = this.memberRepository.findByEmail(email);
    	return member.get();
    }
*/


  //사용자 조회 
    public Member getMember1(String email) {
    	
	  	
   	 Optional<Member> member = this.memberRepository.findByEmail(email);
   	 if (member.isPresent()) {
   		
   		 return member.get();
   	 } else {
   		
   		 throw new DataNotFoundException("siteuser not found ");
   	 }
   	
   }


    
    //비밀번호 수정
	 public void modifyPw(pwdDto pwdDto , Member member) {
		 
		 member.setMemPass(this.passwordEncoder.encode(pwdDto.getNewPwd()));
		 
		 this.memberRepository.save(member);
	 }
	 


	 //임시비밀번호로 비밀번호 수정 
	 public void updatePassword(Long memberId, String memberPassword) {
		    Member member = memberRepository.findById(memberId).orElseThrow();
		    member.setMemPass(memberPassword);
		    memberRepository.save(member);
	 }
	 
	 
	 //3.30 프로필 이미지 업로드
	 public void saveProfileImage(Long memberIdx, MultipartFile file) throws Exception {
	    	Member member = memberRepository.findByIdx(memberIdx);
	        
	    	String oriImgName = file.getOriginalFilename();
	    	
	    	String imgName = fileService.uploadFile(memberImgLocation, oriImgName, file.getBytes());
	        String uploadDir = "/img/profile/" + imgName;
	        
	        member.setImgurl(uploadDir);
	        memberRepository.save(member);

	        FileUploadUtil.saveFile(uploadDir, oriImgName, file);
	  }
	 
	 
}

