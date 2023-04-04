package com.project.social;

import com.project.constant.Role;
import com.project.constant.Social;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter @Setter
public class SocialMember {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   
    private Long idx; //자동으로 늘어나는값
    
    @Email
    @Column(unique = true)
    private String email; //이메일
     
    private String memPass; //비밀번호
    

    private String memName; //이름

    private String memPhone; //휴대폰
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    //3.30 프로필 이미지
    private String imgurl;
    
    //주소

    private String zipcode;

    private String streetAdr;

    private String detailAdr;
    
    //Erd 추가 
    @Enumerated(EnumType.STRING)
	public Social social;

	public String token;
	
	public String getRoleKey() {
		return this.role.getKey();
	}
}
