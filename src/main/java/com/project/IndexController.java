package com.project;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.entity.Member;
import com.project.item.entity.Item;
import com.project.item.repository.ItemRepository;
import com.project.item.service.ItemService;
import com.project.repository.MemberRepository;
import com.project.service.MemberService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class IndexController {


   private final ItemService itemService;
   private final ItemRepository itemRepository;
   private final MemberService memberService;
   
   /*
   @RequestMapping(value = "/")
   public String index() {
      
      
         return "main";
   }
   */
   

   	  //홈 /23.04.01 프로필이미지 관련 principal추가
//	   @RequestMapping(value = "/")
//	   public String main(Principal principal, Model model) {
//	      List<Item> itemList = this.itemRepository.findAll();
//	      model.addAttribute("itemList", itemList);
//	      
//	      if (!(principal == null)) {
//	          Member member = memberService.getMember1(principal.getName());
//	          model.addAttribute("member", member);   
//	      }
//	    
//	      return "main";
//	   }
   



   

		/*
		@RequestMapping(value = "/members/login")
		public String login() {
			return "login";
		}
		*/
		//회원가입 
//		@RequestMapping(value = "/join")
//		public String join() {
//			return "join";
//		}
		
//		//공지사항
//		@RequestMapping(value = "/notice")
//		public String notice() {
//			 
//			return "notice";
//		}
//		
//		//상품리스트
//		@RequestMapping(value = "/list")
//		public String list() {
//			 
//			return "list";
//		}
//		//상품상세
//			@RequestMapping(value = "/product")
//			public String product() {
//				 
//				return "product";
//			}
//			
//			
//		//카카오 로그인
//			@RequestMapping(value = "/kakao")
//			public String kakao() {
//				return "kakao";
//			}
//			
//			
//		//네이버 로그인	
//			@RequestMapping("/naver")
//		    public String naver() {
//		        return "naver";
//		    }
		

	
      /*
      @RequestMapping(value = "/members/login")
      public String login() {
         return "login";
      }
      */
      //회원가입 
//      @RequestMapping(value = "/join")
//      public String join() {
//         return "join";
//      }
      
//      //공지사항
//      @RequestMapping(value = "/notice")
//      public String notice() {
//          
//         return "notice";
//      }
//      
//      //상품리스트
//      @RequestMapping(value = "/list")
//      public String list() {
//          
//         return "list";
//      }
//      //상품상세
//         @RequestMapping(value = "/product")
//         public String product() {
//             
//            return "product";
//         }
//         
//         
//      //카카오 로그인
//         @RequestMapping(value = "/kakao")
//         public String kakao() {
//            return "kakao";
//         }
//         
//         
//      //네이버 로그인   
//         @RequestMapping("/naver")
//          public String naver() {
//              return "naver";
//          }
      

   }

