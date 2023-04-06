package com.project.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.Response;
import com.project.model.KakaoProfile;
import com.project.model.User;
import com.project.repository.UserRepository;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
		//카카오 로그인 서비스
	 	@Autowired
	    UserRepository userRepository; //(1)
	    
	    public OauthToken getAccessToken(String code) {

	        //(2)RsTemplate이용해 URL형식으로 PSOT
	        RestTemplate rt = new RestTemplate();

	        //(3)헤더만들기
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

	        //(4)JSOn형식으로 만들어줌
	        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	        params.add("grant_type", "authorization_code");
	        params.add("client_id", "6b36f27350db8f1bdd0436a0710b7bae");
	        params.add("redirect_uri", "http://localhost:9404/oauth/kakao");
	        params.add("code", code);
	       // params.add("client_secret", "Ka1gVvMMGJcdiv5vghslFvaCM2JS1zwg"); // 생략 가능!

	        //(5)Header와 Body합침
	        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
	                new HttpEntity<>(params, headers);

	        //(6)실제요헝 responseBody응답
	        ResponseEntity<String> accessTokenResponse = rt.exchange(
	                "https://kauth.kakao.com/oauth/token",
	                HttpMethod.POST,
	                kakaoTokenRequest,
	                String.class //응답받을 타입
	        );

	        //(7)
	        ObjectMapper objectMapper = new ObjectMapper();
	        OauthToken oauthToken = null;
	        try {
	            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }

	        return oauthToken; //(8)
	    } 
	    
	    public User saveUser(String token) {

			//(1)
	        KakaoProfile profile = findProfile(token);

			//(2)
	        User user = userRepository.findByKakaoEmail(profile.getKakao_account().getEmail());
	        
	        //(3)회원가입처리 
	        if(user == null) {
	            user = User.builder()
	                    .kakaoId(profile.getId())
	                     //(4)
	                    .kakaoProfileImg(profile.getKakao_account().getProfile().getProfile_image_url())
	                    .kakaoNickname(profile.getKakao_account().getProfile().getNickname())
	                    .kakaoEmail(profile.getKakao_account().getEmail())
	                     //(5)
	                    .userRole("ROLE_USER").build();
	            
	            userRepository.save(user);
	        }

	        return user;
	    }
	    
	    
	    //(1-1)동의항목 가져오기 
	    public KakaoProfile findProfile(String token) {
	        
	        System.out.println("");
	        //(1-2)
	        RestTemplate rt = new RestTemplate();

			//(1-3)
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", "Bearer " + token); //(1-4)
	        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

			//(1-5)
	        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
	                new HttpEntity<>(headers);

			//(1-6)유저정보조회
	        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
	        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
	                "https://kapi.kakao.com/v2/user/me",
	                HttpMethod.POST,
	                kakaoProfileRequest,
	                String.class
	        );

			//(1-7)
	        ObjectMapper objectMapper = new ObjectMapper();
	        KakaoProfile kakaoProfile = null;
	        try {
	            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }

	        return kakaoProfile;
	        
	    }
	    //로그아웃 
	    
	    public void kakaoLogout(String access_Token) {
	        String reqURL = "https://kapi.kakao.com/v1/user/logout";
	        try {
	            URL url = new URL(reqURL);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
	            
	            int responseCode = conn.getResponseCode();
	            System.out.println("responseCode : " + responseCode);
	            
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            
	            String result = "";
	            String line = "";
	            
	            while ((line = br.readLine()) != null) {
	                result += line;
	            }
	            System.out.println(result);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	    
	    
	    
	    
}
