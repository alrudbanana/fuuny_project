package com.project.dto;

import org.hibernate.validator.constraints.Length;

import com.project.constant.Role;
import com.project.entity.Member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter
public class MemberDto {
		
	    private Long idx; 
	    
	    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
	    private String email; //이메일
	    
	    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
	    private String memPass; //비밀번호
	   
	    @NotEmpty(message = "이름은 필수 입력 값입니다.")
	    private String memName; //이름

	    @NotEmpty(message = "핸드폰 번호는 필수 입력 값입니다.")
	    private String memPhone; //휴대폰
	    
	    private Role role;
	    
	    @NotEmpty(message = "주소는 필수 입력 값입니다.")
	    private String zipcode;

	    private String streetAdr;
	    
	    private String detailAdr;
	    
	    //3.30 프로필 이미지
	    private String imgurl;
	    
	    public Member saveMemberProfile(MemberDto memberProfile) {
	    	
	    	Member member = new Member();
	    	member.setImgurl(memberProfile.getImgurl());
	    	
	    	return member;
	    }
	    
	}
