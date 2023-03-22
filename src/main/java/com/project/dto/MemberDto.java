package com.project.dto;

import com.project.Role;

import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter
public class MemberDto {

	    private Long idx; 
	    
	    private String email; //이메일
	    
	    private String memPass; //비밀번호

	    private String memName; //이름

	    private String memPhone; //휴대폰
	    
	    private Role role;

	    private String zipcode;

	    private String streetAdr;
	    
	    private String detailAdr;
	    
	}

	

