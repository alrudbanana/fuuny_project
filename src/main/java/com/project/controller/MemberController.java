package com.project.controller;
import java.security.Principal;

import java.util.List;


import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;



import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.Email.dto.MailDto;
import com.project.Email.service.EmailService;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;





import com.project.dto.MemberDto;
import com.project.dto.MemberFormDto;
import com.project.dto.pwdDto;
import com.project.entity.Member;
import com.project.entity.Question;
import com.project.repository.MemberRepository;
import com.project.repository.QuestionRepository;
import com.project.model.KakaoProfile;

import com.project.service.MemberService;
import com.project.service.QuestionService;

import jakarta.transaction.Transactional;


import jakarta.servlet.http.HttpSession;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
	
    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final EmailService emailService;
    
    private final QuestionService questionService;
    
    private final QuestionRepository questionRepository;
    
    //로그인 
    @GetMapping(value = "/login")
	public String login() {
		return "login";
	}
    //로그아웃 
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션을 삭제함
        return "redirect:/";
    }
    //로그인 에러 
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "login";
    }

    //회원가입 
    @PostMapping("/join")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
       
    	System.out.println("컨트롤러 호출됨 ");
    	System.out.println(memberFormDto.getMemName());
    	
    	if(bindingResult.hasErrors()){
            return "join";
        }
    	
       try {
    	 
    	  this.memberService.saveMember(memberFormDto);
    	  
       }catch(Exception e) {
    	   model.addAttribute("save_errors","아이디 혹은 이메일 중복");
    	   return "join";
       }

        return "redirect:/";
    }

    //이메일 보내기
    @Transactional
    @PostMapping("/sendEmail") //members/sendEmail
    public String sendEmail(@RequestParam("memberEmail") String memberEmail){
        // 임시 비밀번호 생성 & 임시비밀번호로 변경 & 메일 내용 담기
        MailDto dto = emailService.createMailAndChangePassword(memberEmail);
        // 메일 보내기
        emailService.mailSend(dto);
        System.out.println("메일전송완료");
        return "login";
    }
    
    //이메일 중복 체크 
    @PostMapping("/emailDuplication") //members/emailDuplication
    public @ResponseBody
    String emailDuplication(@RequestParam("memberEmail") String memberEmail) {
        String result = emailService.emailDuplication(memberEmail);
        return result;
    }

    //회원가입 뷰 페이지 출력
    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "memberForm";
    }

    @PostMapping(value="/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
    	System.out.println("컨트롤러 호출됨 ");
    	System.out.println(memberFormDto.getMemName());
    	
    	if(bindingResult.hasErrors()) {
			return "memberForm";
		}
    	if(!memberFormDto.getMemPass().equals(memberFormDto.getMemPass2())) {
    		 bindingResult.rejectValue("memPass2", "passwordInCorrect", 
                     "2개의 패스워드가 일치하지 않습니다.");
             return "memberForm";
    	}
       try {
    	   this.memberService.saveMember(memberFormDto);
    	  
       }catch(Exception e) {
			model.addAttribute("memberFormDto", "아이디 혹은 이메일 중복.");
			return "memberForm";
		}
    return "redirect:/";
   
    }

    //마이페이지 조회
    @GetMapping("/detail")
	public String detail(Model model, Principal principal) {

		String email = principal.getName();
		
		Member member = this.memberService.getMember1(email);
		model.addAttribute("member",member);

		return "mypage"; 
	}
	
	
	
	//회원정보 수정
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/modify")
	public String memberModify(MemberDto memberDto, Principal principal) {

		 String name = principal.getName();
		 Optional<Member> modify = memberRepository.findByEmail(name);
		 Member member = modify.get();
		
		memberDto.setEmail(member.getEmail());
		memberDto.setMemName(member.getMemName());
		memberDto.setMemPhone(member.getMemPhone());
		memberDto.setZipcode(member.getZipcode());
		memberDto.setStreetAdr(member.getStreetAdr());
		memberDto.setDetailAdr(member.getDetailAdr());

		return "mypagemodify";
		}


	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/modify")
	public String memberModify( @Valid MemberDto memberDto, BindingResult bindingResult, 
			Principal principal,  String email , Model model) {
		
		 Optional<Member> modify = memberRepository.findByEmail(email);
		 Member member = modify.get();
		 String pass = modify.get().getMemPass();
		 
		 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		 if(!encoder.matches(memberDto.getMemPass(), pass)){
					 
			 model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다. ");
		
			 return "mypagemodify";
			 
		 }
		
		
		if(bindingResult.hasErrors()) {
			return "mypagemodify";
		}
		
		
		this.memberService.modify(member, memberDto);
		return String.format("redirect:/members/detail");
	}
	
	//비밀번호 변경하기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/pwd")
	public String memberModifypwd(pwdDto pwdDto ,Principal principal) {

		 String name = principal.getName();
		 Optional<Member> modifypw = memberRepository.findByEmail(name);
		 
		 Member member = modifypw.get(); 	
		 
			return "pwdmodify";
	}

	

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/pwd")
	public String memberModifypwd( @Valid pwdDto pwdDto, BindingResult bindingResult, 
			Principal principal, Model model) {


			 String name = principal.getName();
			 Optional<Member> modifypw = memberRepository.findByEmail(name);
			 Member member = modifypw.get();
			 String pass = modifypw.get().getMemPass();
			 
			 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			 
			 if(!encoder.matches(pwdDto.getNowPwd(), pass)){
		
				 model.addAttribute("errorMessage", "현재의 비밀번호와 일치하지 않습니다. ");
				 
				 return "pwdmodify";
				 
			 }
			 
		    	if(!pwdDto.getNewPwd().equals(pwdDto.getNewPwd2())) {
		    		model.addAttribute("errorMessage", "2개의 패스워드가 일치하지 않습니다.");
		             return "pwdmodify";
		    	}
			if(bindingResult.hasErrors()) {
				return "pwdmodify";
			}
						
			this.memberService.modifyPw(pwdDto , member);
			return String.format("redirect:/members/detail");
	}
		
	//회원 탈퇴
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{idx}")
	public String questionDelete(Principal principal, @PathVariable("idx") Long idx) {
		Member member = this.memberService.getMember(idx);
		this.memberService.delete(member);
		 return "redirect:/";
	}


    
    
    //3.30 프로필 업데이트
    @PostMapping("/profile/img/{member}")
    public String uploadImg(@PathVariable("member") String memberIdx, @RequestParam("file") MultipartFile file) {
        Long id = Long.valueOf(memberIdx);
        try {
            memberService.saveProfileImage(id, file);
            return "redirect:/members/detail";
        } catch (Exception e) {
        	return "redirect:/members/detail";
        }
    }
    

    //마이페이지에서 문의내역조회
    @GetMapping("/question/list/{idx}")
    
    public String list (Model model ,   @PathVariable("idx") Long idx , @RequestParam(value="page", defaultValue="0") int page , Principal principal) {
      
       
      String email = principal.getName();
      
      Member member = this.memberService.getMember1(email);
      
       
      Page<Question> paging = questionService.myquestionlist( page , idx);
      
      model.addAttribute("paging", paging);
      model.addAttribute("member",member);
      
      return "mypagequestionlist";
    
}

}

