package com.project.dto;

import org.hibernate.validator.constraints.Length;

import com.project.constant.Role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberFormDto {
	
	@NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String memName;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String memPass;
    
    @NotEmpty(message = "비밀번호를 동일하게 입력해주세요!")
    private String memPass2;
   
    @NotEmpty(message = "핸드폰 번호는 필수 입력 값입니다.")
    private String memPhone;
 
	@NotEmpty(message = "주소는 필수 입력 값입니다.")
	private String zipcode;
    private String streetAdr;
    private String detailAdr;
    
    private Role role;
    
   
    


}