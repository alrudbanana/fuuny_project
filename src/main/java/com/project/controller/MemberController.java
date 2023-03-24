package com.project.controller;
import java.security.Principal;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.project.dto.MemberDto;
import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
	
    private final MemberService memberService;

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


	@GetMapping("/detail")
	public String detail(Model model, Principal principal) {

		// 서비스 클래스의 메소드 호출 : 상세페이지 보여달라
		String email = principal.getName();
		
		Member member = this.memberService.getMember1(email);
		model.addAttribute("member",member);

		return "mypage"; // template : question_detail.html

	}
	
	
	
	

	
@GetMapping(value = "/modify")
public String memberModify(MemberDto memberDto, Principal principal) {

	String email = principal.getName();
	
	Member member = this.memberService.getMember1(email);
	
	
	memberDto.setEmail(member.getEmail());
	memberDto.setMemPass(member.getMemPass());
	memberDto.setMemName(member.getMemName());
	memberDto.setMemPhone(member.getMemPhone());
	memberDto.setZipcode(member.getZipcode());
	memberDto.setStreetAdr(member.getStreetAdr());
	memberDto.setDetailAdr(member.getDetailAdr());

	return "mypagemodify";
	}



@PreAuthorize("isAuthenticated()")
@PostMapping(value = "/modify")
public String memberModify( @Valid MemberDto memberDto, BindingResult bindingResult, MemberFormDto memberFormDto, Member member, Principal principal) {

	if(bindingResult.hasErrors()) {
		return "mypagemodify";
	}
	
	this.memberService.modify(member, memberDto.getEmail(), memberDto.getMemPass(), memberDto.getMemName(), memberDto.getMemPhone(), memberDto.getZipcode(), memberDto.getStreetAdr(), memberDto.getDetailAdr());
	return "redirect:/mypage";
}


@PreAuthorize("isAuthenticated()")
@GetMapping("/delete")
public String Delete(Principal principal) {
	
	String email = principal.getName();
	
	Member member = this.memberService.getMember1(email);

this.memberService.delete(member);
return "redirect:/";
}




//
//@GetMapping("pw-change")
//public ModelAndView pwChange() {
//	return new ModelAndView ("user/pw-chage");
//}

//@PostMapping("/checkPw")
//public String checkPw(@RequestBody String pw, HttpSession session) throws Exception {
//	
//	String result = null;
//	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//	
//	MemberDto dbUser = (MemberDto)session.getAttribute("login");
//	
//	if(encoder.matches(pw, dbUser.getMemPass())) {
//		result = "pwConfirmOK";
//	}else {
//		result = "pwConfirmNO";
//	}
//	
//	return result;
//}

//public String PwChange(@RequestBody MemberDto email, HttpSession session)throws Exception {
//	
//	memberService.modifyPw(email);
//	
//	LoginVO modifyUser = new LoginVO();
//	modifymember.setEmail()
//	
//}





//로그인 
    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "login";
    }
    
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
 
}
   

