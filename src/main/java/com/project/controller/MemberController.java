package com.project.controller;
import java.security.Principal;

import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;



import java.security.Principal;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;



import com.project.dto.MemberDto;
import com.project.dto.MemberFormDto;
import com.project.entity.Member;
import com.project.model.KakaoProfile;
import com.project.service.MemberService;


import jakarta.servlet.http.HttpSession;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
	
    private final MemberService memberService;


    @GetMapping(value = "/login")
	public String login() {
		return "login";
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


	@GetMapping("/detail")
	public String detail(Model model, Principal principal) {

		String email = principal.getName();
		
		Member member = this.memberService.getMember1(email);
		model.addAttribute("member",member);

		return "mypage"; 

	}
	
	

	
	
	@GetMapping(value = "/detail/{idx}")
	public String detail(Model model, @PathVariable("idx") Long idx) {

		// 서비스 클래스의 메소드 호출 : 상세페이지 보여달라
		Member b = this.memberService.getMember(idx);

		// Model 객체에 담아서 클라이언트에게 전송
		model.addAttribute("member", b);

		return "mypage"; 

	}
	
	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/modify/{idx}")
	public String memberModify(MemberDto memberDto, @PathVariable("idx") Long idx, Principal principal) {

		Member member = this.memberService.getMember(idx);
		
		memberDto.setEmail(member.getEmail());
		memberDto.setMemName(member.getMemName());
		memberDto.setMemPhone(member.getMemPhone());
		memberDto.setZipcode(member.getZipcode());
		memberDto.setStreetAdr(member.getStreetAdr());
		memberDto.setDetailAdr(member.getDetailAdr());

		return "mypagemodify";
		}



	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/modify/{idx}")
	public String memberModify( @Valid MemberDto memberDto, BindingResult bindingResult, Principal principal, Member member ,  @PathVariable("idx") Long idx ) {

		
		if(bindingResult.hasErrors()) {
			return "mypagemodify";
		}
		
		
		this.memberService.modify(member, memberDto, memberDto.getEmail(), memberDto.getMemName(), memberDto.getMemPhone(), memberDto.getZipcode(), memberDto.getStreetAdr(), memberDto.getDetailAdr());
		return String.format("redirect:/members/detail/%s", idx);
	}
	
	
	

//	@PreAuthorize("isAuthenticated()")
//	@PostMapping(value = "/modify/pwd")
//	public String memberModifypwd( @Valid @RequestBody MemberDto memberDto, BindingResult bindingResult,  Principal principal) {
//
//		if(bindingResult.hasErrors()) {
//			return "pwdmodify";
//			
//		}
//		
//		return "pwdmodify";
//		
//	}
	

	
//@GetMapping(value = "/modify")
//public String memberModify(MemberDto memberDto, Principal principal) {
//
//	String email = principal.getName();
//	
//	Member member = this.memberService.getMember1(email);
//	
//	
//	memberDto.setEmail(member.getEmail());
//	memberDto.setMemPass(member.getMemPass());
//	memberDto.setMemName(member.getMemName());
//	memberDto.setMemPhone(member.getMemPhone());
//	memberDto.setZipcode(member.getZipcode());
//	memberDto.setStreetAdr(member.getStreetAdr());
//	memberDto.setDetailAdr(member.getDetailAdr());
//
//	return "mypagemodify";
//	}
//
//
//
//@PreAuthorize("isAuthenticated()")
//@PostMapping(value = "/modify")
//public String memberModify( @Valid MemberDto memberDto, BindingResult bindingResult, MemberFormDto memberFormDto, Member member, Principal principal) {
//
//	if(bindingResult.hasErrors()) {
//		return "mypagemodify";
//	}
//	
//	this.memberService.modify(member, memberDto.getEmail(), memberDto.getMemPass(), memberDto.getMemName(), memberDto.getMemPhone(), memberDto.getZipcode(), memberDto.getStreetAdr(), memberDto.getDetailAdr());
//	return "redirect:/mypage";
//}


//@PreAuthorize("isAuthenticated()")
//@GetMapping("/delete")
//public String Delete(Principal principal) {
//	
//	String email = principal.getName();
//	
//	Member member = this.memberService.getMember1(email);
//
//this.memberService.delete(member);
//return "redirect:/";
//}

@PreAuthorize("isAuthenticated()")
@GetMapping("/delete/{idx}")
public String questionDelete(Principal principal, @PathVariable("idx") Long idx) {
	Member member = this.memberService.getMember(idx);
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



    //로그인 



  


    
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
//    
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


 
    
 
    }

  

