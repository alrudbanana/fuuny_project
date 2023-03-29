package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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