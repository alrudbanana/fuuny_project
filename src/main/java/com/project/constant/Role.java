package com.project.constant;

import lombok.Getter;

@Getter
public enum Role {
	ADMIN("ADMIN"),
	USER("USER"),
	SELLER("SELLER");
	
	Role(String value){
		this.value=value;
	}
	private String code;
	private String key;
	private String value;
}
