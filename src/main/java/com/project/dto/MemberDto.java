package com.project.dto;

import com.project.Role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter
public class MemberDto {

	    private Long idx; 
	    
	    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
	    private String email; //이메일
	    
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
	    
	}
