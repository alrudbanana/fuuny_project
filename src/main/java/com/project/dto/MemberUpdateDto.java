package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {
    private Long idx;
    private String email;
    private String memPass;
    private String memName;
    private String memPhone;

}