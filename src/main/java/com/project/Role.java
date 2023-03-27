package com.project;

import lombok.Getter;

@Getter
public enum Role {
	ADMIN("ADMIN"),
	USER("USER"),
	SELLER("SELLER");
	
	Role(String value){
		this.value=value;
	}
	
	private String key;
	private String value;
}
