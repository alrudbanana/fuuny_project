package com.project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
	
	 @PostMapping("/payment")
	    public Map<String, String> payment(@RequestBody Map<String, String> data) {
	        // 데이터 처리 로직
	        Map<String, String> response = new HashMap<>();
	        response.put("id", data.get("id"));
	        response.put("itemNm", data.get("itemNm"));
	        response.put("itemPrice", data.get("itemPrice"));
	        response.put("email", data.get("email"));
	        response.put("memName", data.get("memName"));
	        response.put("memPhone", data.get("memPhone"));
	        return response;
	    }

}
