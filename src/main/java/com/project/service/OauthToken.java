package com.project.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
//post요청해서 받은 값 저장
public class OauthToken {
	 	private String access_token;
	    private String token_type;
	    private String refresh_token;
	    private int expires_in;
	    private String scope;
	    private int refresh_token_expires_in;
}
