package com.project.Error;

public class ErpprResponse {

	private String message;
	
	public void ErrorResponse (String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
