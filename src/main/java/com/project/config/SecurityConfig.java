package com.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.model.KakaoProfile;
import com.project.service.CustomOAuth2MemberService;

import lombok.RequiredArgsConstructor;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	 @Autowired
	 private CustomOAuth2MemberService customOAuth2MemberService;
	 
	
	
	 @Bean
	    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http.authorizeHttpRequests().requestMatchers(
	        	
	                new AntPathRequestMatcher("/**")).permitAll()
	        .and()
	        .csrf().ignoringRequestMatchers(
	                new AntPathRequestMatcher("/h2-console/**"))
	        .and()
	        .csrf().disable()
	        .headers()
	        .addHeaderWriter(new XFrameOptionsHeaderWriter(
	                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
	        //로그인 
	    	.and()
			.formLogin()
			.loginPage("/members/login")
			.loginProcessingUrl("/members/login")
			.defaultSuccessUrl("/")
			
			//로그아웃
	        .and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true)
	        
	        //oauth2로그인
			.and()
			.oauth2Login()
				.loginPage("/login")
				.defaultSuccessUrl("/social/login")
				.userInfoEndpoint()
				.userService(customOAuth2MemberService)
				;
	        
	        return http.build();
	        
	        
	    
	        
	    }

	    
		 @Bean
		    public PasswordEncoder passwordEncoder() {
		        return new BCryptPasswordEncoder();
		}
		 
		 @Bean
			AuthenticationManager authenticateionManager(AuthenticationConfiguration authenticationConfiguration) 
			throws Exception{
				return authenticationConfiguration.getAuthenticationManager();
			}

	}