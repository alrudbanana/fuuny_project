package com.project.Email.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.Email.dto.MailDto;
import com.project.dto.MemberMapperRepository;
import com.project.entity.Member;
import com.project.repository.MemberEmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
	
	private final MemberEmailRepository mr;
	private final MemberMapperRepository mmr;
	//인증메일 발송
	private final JavaMailSender javaMailSender;
	
	 //인증번호 생성
	//epw에 createKey()메소드로 생성된 6자리 랜덤 문자열을 ePw변수에 할당한다. 
    private final String ePw = createKey();

    @Value("${spring.mail.username}")
    private String id;

    //메일 보내는 메소드 
    public MimeMessage createMessage(String to)throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ to);
        log.info("인증 번호 : " + ePw);
        
        MimeMessage  message = javaMailSender.createMimeMessage(); //MimeMessage객체를 생성 

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상, 수신자 설정
        message.setSubject("ㅇㅇㅇ 회원가입 인증 코드: "); //메일 제목 //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg=""; //멩;ㄹ 내용을 msg라는 변수에 Html 형식으로 작성
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(id,"prac_Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름을 설정하고 message객체를 반환함

        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer(); //SpringBuffer를 사용하여 생성된 숫자 6자리를 담고, Randomㄷ클래스를 사용해 무작위로 0~9를 생성한다. 
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10))); //.append : StringBuffer 객체 Key에 append를 넣어줌 
        }
        return key.toString(); //.toString 메소드는 객체에 저장된 문자열 값을 반환하고 객체에 저장된 문자열 ㅂ값을 String으로 변환시켜주는것. 저장된 문자열 값을 반환하기 위해 toString메서드 사용 
    }

    /*
        메일 발송
        sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소
        MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
        bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
     */
    public String sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException(); //예외가 발생했다는 것을 알리는 코드 . 호출자에게 잘못된 인수를 전달했다는 것을 알림 
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }
    
    
 // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경

    public MailDto createMailAndChangePassword(String memberEmail) {
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(memberEmail);
        dto.setTitle("Cocolo 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. Cocolo 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + str + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
        updatePassword(str,memberEmail);
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
    public void mailSend(MailDto mailDTO) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("pratt33@naver.com");
        message.setReplyTo("pratt33@naver.com");
        System.out.println("message"+message);
        javaMailSender.send(message);
    }

    //비밀번호 변경
    public void updatePassWord(Long memberId, String memberPassword) {
        mmr.updatePassword(memberId,memberPassword);
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
	

