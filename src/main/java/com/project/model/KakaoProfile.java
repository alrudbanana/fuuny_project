package com.project.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class KakaoProfile {
    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    public static class Properties {
        private String nickname;
        private String profile_image; // 이미지 경로 필드1
        private String thumbnail_image;
    }

    @Data
    public static class KakaoAccount {
        private Boolean profile_nickname_needs_agreement;
        private Boolean profile_image_needs_agreement;
        private Profile profile;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;

        @Data
        public static class Profile {
            private String nickname;
            private String thumbnail_image_url;
            private String profile_image_url; // 이미지 경로 필드2
            private Boolean is_default_image;
        }
    }
}
