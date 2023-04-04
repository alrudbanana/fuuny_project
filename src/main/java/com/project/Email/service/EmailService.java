package com.project.Email.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.Email.dto.MailDto;
import com.project.entity.Member;
import com.project.repository.MemberEmailRepository;
import com.project.service.MemberService;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
public class EmailService {
	@Autowired
	MemberService mmr;
	private final MemberEmailRepository mr;
	//인증메일 발송
	private final JavaMailSender javaMailSender;
	


 // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경


	@Autowired
	private PasswordEncoder passwordEncoder;
	
 // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경

	public MailDto createMailAndChangePassword(String memberEmail) {
	    String tempPassword = getTempPassword();
	    String encodedPassword = passwordEncoder.encode(tempPassword); // 임시 비밀번호를 암호화합니다.
	    MailDto dto = new MailDto();
	    dto.setAddress(memberEmail);
	    dto.setTitle("Funny 임시비밀번호 안내 이메일 입니다.");
	    dto.setMessage("안녕하세요. Funny 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
	            + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
	    updatePassword(encodedPassword,memberEmail); // 암호화된 비밀번호를 DB에 저장합니다.
	    return dto;
	}


    //임시 비밀번호로 업데이트
    public void updatePassword(String str, String userEmail){
        String memberPassword = str;
        Long memberId = mr.findByEmail(userEmail).getIdx();
        mmr.updatePassword(memberId,memberPassword);
    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
    // 메일보내기
    public void mailSend(MailDto mailDTO)  {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("pratt3343@gmail.com");
        message.setReplyTo("pratt3343@gmail.com");
        System.out.println("message"+message);
        javaMailSender.send(message);
    }

    //이메일 중복 체크 
    public String emailDuplication(String email) {
        Member member = mr.findByEmail(email);
        if (member == null) {
            return "ok";
        } else {
            return "no";
        }
    }

}
	

