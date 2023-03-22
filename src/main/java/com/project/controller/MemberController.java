package com.project.controller;
import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;


import com.project.dto.MemberDto;
import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.service.MemberService;

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


	@GetMapping(value = "/detail/{idx}")
	public String detail(Model model, @PathVariable("idx") Long idx) {

		// 서비스 클래스의 메소드 호출 : 상세페이지 보여달라
		Member b = this.memberService.getMember(idx);

		// Model 객체에 담아서 클라이언트에게 전송
		model.addAttribute("member", b);

		return "mypage"; // template : question_detail.html

	}
	
	
	
	

@PreAuthorize("isAuthenticated()")
@GetMapping(value = "/modify/{idx}")
public String memberModify(MemberFormDto memberFormDto, @PathVariable("idx") Long idx, Principal principal) {

	Member member = this.memberService.getMember(idx);
	
	memberFormDto.setEmail(member.getEmail());
	memberFormDto.setMemPass(member.getMemPass());
	memberFormDto.setMemName(member.getMemName());
	memberFormDto.setMemPhone(member.getMemPhone());
	memberFormDto.setZipcode(member.getZipcode());
	memberFormDto.setStreetAdr(member.getStreetAdr());
	memberFormDto.setDetailAdr(member.getDetailAdr());

	return "/modify/{idx}";
	}



@PreAuthorize("isAuthenticated()")
@PostMapping(value = "/modify/{idx}")
public String memberModify( @Valid MemberFormDto memberFormDto, BindingResult bindingResult, Member member, MemberDto memberDto, Principal principal, @PathVariable("idx") Long idx) {
	if(bindingResult.hasErrors()) {
		return "/modify/{idx}";
	}

	this.memberService.modify(member, memberDto.getEmail(), memberDto.getMemPass(), memberDto.getMemName(), memberDto.getMemPhone(), memberDto.getZipcode(), memberDto.getStreetAdr(), memberDto.getDetailAdr());
	return String.format("redirect:/members/detail/%s", idx);
}

}
    //로그인 
//    @GetMapping(value = "/login")
//    public String login(){
//        return "login";
//    }
//
//    
//    @GetMapping(value = "/login/error")
//    public String loginError(Model model){
//        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
//        return "login";
//    }
    
//    @PostMapping("/join")
//    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
//       
//    	System.out.println("컨트롤러 호출됨 ");
//    	System.out.println(memberFormDto.getName());
//    	
//    	if(bindingResult.hasErrors()){
//            return "join";
//        }
//    	
//       try {
//    	 
//    	  this.memberService.save(memberFormDto);
//    	  
//       }catch(Exception e) {
//    	   model.addAttribute("save_errors","아이디 혹은 이메일 중복");
//    	   return "join";
//       }
//
//        return "redirect:/";
//    }
 

   

