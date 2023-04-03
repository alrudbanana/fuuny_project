package com.project.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.model.KakaoProfile;
import com.project.model.User;
import com.project.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
 
    public CustomOAuth2UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
 
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // OAuth2 공급자 서비스로부터 받아온 사용자 정보
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
 
        // OAuth2 공급자 서비스 타입
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
 
        // OAuth2 공급자에서 제공하는 사용자 식별자 (예: 카카오 계정의 ID)
        String providerUserId = oAuth2User.getName();
 
        // DB에 저장되어 있는 사용자 정보 조회
        String kakaoEmail = KakaoProfile.getEmail();
        Optional<User> userOptional = userRepository.findByKakaoEmail(kakaoEmail);
 
        User user;
        if (userOptional.isPresent()) {
            // 사용자 정보가 이미 존재하는 경우
            user = userOptional.get();
            updateExistingUser(user, oAuth2User);
        } else {
            // 새로운 사용자 정보를 DB에 저장
            user = registerNewUser(oAuth2User, registrationId);
        }
 
        return UserDetailsImpl.create(user, oAuth2User.getAttributes());
    }
 
    private User registerNewUser(OAuth2User oAuth2User, String registrationId) {
        // 새로운 사용자 정보 생성
        User user = new User();
        user.setProviderId(oAuth2User.getName());
        user.setProvider(registrationId);
        user.setEmail(oAuth2User.<String>getAttribute("email"));
        user.setName(oAuth2User.<String>getAttribute("name"));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRoles(Collections.singletonList(Role.USER));
 
        // DB에 사용자 정보 저장
        return userRepository.save(user);
    }
 
    private void updateExistingUser(User user, OAuth2User oAuth2User) {
        // 사용자 정보 업데이트
        user.setEmail(oAuth2User.<String>getAttribute("email"));
        user.setName(oAuth2User.<String>getAttribute("name"));
        userRepository.save(user);
    }
}

