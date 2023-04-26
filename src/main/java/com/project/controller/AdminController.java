package com.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


import com.project.constant.ItemSellStatus;
import com.project.constant.Role;
import com.project.constant.RoleMemberCode;
import com.project.dto.AdminItemDto;
import com.project.dto.NoticeFormDto;
import com.project.entity.Member;
import com.project.entity.Notice;
import com.project.item.dto.ItemFormDto;
import com.project.item.entity.Item;
import com.project.service.AdminService;
import com.project.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

   private final AdminService adminService;
   private final MemberService memberService;


   @GetMapping(value = "/main")
   public String adminMain(Model model, Optional<Integer> page) {
      List<ItemSellStatus> aa = new ArrayList();
      aa.add(ItemSellStatus.WAIT);
      aa.add(ItemSellStatus.CLOSE);
      aa.add(ItemSellStatus.CONFIRM);
      aa.add(ItemSellStatus.REFUSE);
      aa.add(ItemSellStatus.SELL);
      aa.add(ItemSellStatus.SOLD_OUT);

      System.out.println("컨트롤러 호출됨");

      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("id"));
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9, Sort.by(sorts));

      System.out.println("pageable 잘 호출됨 ");

      Page<AdminItemDto> itemCondition = this.adminService.getAdminItemPageNew(aa, pageable);

      System.out.println("itemcondition 잘 호출됨");

      model.addAttribute("items", itemCondition);
      model.addAttribute("maxPage", 5);

      System.out.println("백엔드 로직 완료 ");

      return "admin/adminMain";
   }

   
   
   @GetMapping(value = "/funding")
   public String adminFunding(Model model, Optional<Integer> page) {
      List<ItemSellStatus> aa = new ArrayList();
      aa.add(ItemSellStatus.WAIT);

      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("id"));
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9, Sort.by(sorts));

      Page<AdminItemDto> itemCondition = this.adminService.getAdminItemPageNew(aa, pageable);

      model.addAttribute("items", itemCondition);
      model.addAttribute("maxPage", 5);
      return "admin/adminfunding";
   }

   @GetMapping(value = "/funding/approve")
   public String adminFundingApprove(Model model, Optional<Integer> page) {
      List<ItemSellStatus> aa = new ArrayList();
      aa.add(ItemSellStatus.CONFIRM);
      aa.add(ItemSellStatus.SELL);
      aa.add(ItemSellStatus.SOLD_OUT);

      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("id"));
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9, Sort.by(sorts));

      Page<AdminItemDto> itemCondition = this.adminService.getAdminItemPageNew(aa, pageable);

      model.addAttribute("items", itemCondition);
      model.addAttribute("maxPage", 5);
      return "admin/adminFundingApprove";
   }

   @GetMapping(value = "/funding/end")
   public String adminFundingEnd(Model model, Optional<Integer> page) {
      List<ItemSellStatus> aa = new ArrayList();
      aa.add(ItemSellStatus.CLOSE);

      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("id"));
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9, Sort.by(sorts));

      Page<AdminItemDto> itemCondition = this.adminService.getAdminItemPageNew(aa, pageable);

      model.addAttribute("items", itemCondition);
      model.addAttribute("maxPage", 5);
      return "admin/adminFundingEnd";
   }

   @GetMapping(value = "/funding/refuse")
   public String adminFundingRefuse(Model model, Optional<Integer> page) {
      List<ItemSellStatus> aa = new ArrayList();
      aa.add(ItemSellStatus.REFUSE);

      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("id"));
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9, Sort.by(sorts));

      Page<AdminItemDto> itemCondition = this.adminService.getAdminItemPageNew(aa, pageable);

      model.addAttribute("items", itemCondition);
      model.addAttribute("maxPage", 5);
      return "admin/adminFundingRefuse";
   }

   // 2023.03.25 유저관리 - 페이징 처리
   @GetMapping(value = "/userManage")
   public String adminUserManage(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
      Page<Member> paging = this.adminService.getUserList(page);
      model.addAttribute("paging", paging);
      return "admin/adminUserManage";
   }

   // 공지 관리로 이동
   @GetMapping(value = "/notice")
   public String adminNotice(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
      Page<Notice> paging = this.adminService.getList(page);
      model.addAttribute("paging", paging);
      return "admin/adminNotice";
   }

   // 공지 디테일로 이동
   @GetMapping(value = "/notice/detail/{id}")
   public String noticeDetail(Model model, @PathVariable("id") Integer id) {
      Notice notice = this.adminService.getNotice(id);
      model.addAttribute("notice", notice);
      return "admin/adminNoticeDetail";
   }
   
   // 메인공지글로 이동
   @GetMapping(value = "/notice/user")
   public String adminNoticeuser(Model model, @RequestParam(value = "page", defaultValue = "0") int page , Principal principal ) {

      String email = principal.getName();
      Member member = this.memberService.getMember1(email);
      Page<Notice> paging = this.adminService.getList(page);
      
      model.addAttribute("paging", paging);
      model.addAttribute("member",member);
      return "notice";
   }
   
   // 메인공지 디테일로 이동
   @GetMapping(value = "/notice/user/detail/{id}")
   public String noticeuserDetail(Model model, @PathVariable("id") Integer id , Principal principal) {
      
     String email = principal.getName();
     Member member = this.memberService.getMember1(email);
      
     Notice notice = this.adminService.getNotice(id);
      model.addAttribute("notice", notice);
      model.addAttribute("member",member);
      return "noticedetail";
   }

   // 공지 생성으로 이동
   @GetMapping(value = "/notice/write")
   public String noticeWrite() {
      return "admin/adminNoticeWriteForm";
   }

   // 공지생성
   @PostMapping(value = "/notice/write")
   public String noticeWrite(@RequestParam String title, @RequestParam String content) {
      this.adminService.writeNotice(title, content);
      return "redirect:/admin/notice";
   }

   // 공지
   // 공지 수정으로 이동
   @GetMapping(value = "/notice/modify/{id}")
   public String noticeModify(Model model, @PathVariable("id") Integer id) {
      Notice notice = this.adminService.getNotice(id);
      model.addAttribute("notice", notice);
      return "admin/adminNoticeModify";
   }

   // 공지 수정
   @PostMapping(value = "/notice/modify/{id}")
   public String noticeModify(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Principal principal,
         @PathVariable("id") Integer id) {

      if (bindingResult.hasErrors()) {
         return "admin/adminNoticeModify";
      }
      Notice notice = this.adminService.getNotice(id);
      this.adminService.modifyNotice(notice, noticeFormDto.getTitle(), noticeFormDto.getContent());

      return String.format("redirect:/admin/notice/detail/%s", id);
   }

   // 공지 삭제
   @GetMapping(value = "/notice/delete/{id}")
   public String noticeDelete(@PathVariable("id") Integer id) {
      Notice notice = this.adminService.getNotice(id);
      this.adminService.deleteNotice(notice);
      return "redirect:/admin/notice";
   }

   // 2023.03.27 유저 권한 수정 완료
   @PostMapping("/member/modify")
   public String memberRoleModify(@RequestParam(value = "param1") Long param1,
         @RequestParam(value = "param2") Role param2) {

      System.out.println("param1 : " + param1 + ", param2 : " + param2);

      this.adminService.modifyMemberRole(param1, param2);
      return "redirect:/admin/userManage";
   }

   // 셀렉트 타임리프로 구현
   @ModelAttribute("roleMemberCode")
   public List<RoleMemberCode> roleMemberCode() {
      List<RoleMemberCode> roleMemberCode = new ArrayList<>();
      roleMemberCode.add(new RoleMemberCode("USER", "일반사용자"));
      roleMemberCode.add(new RoleMemberCode("SELLER", "판매자"));
      roleMemberCode.add(new RoleMemberCode("ADMIN", "관리자"));
      return roleMemberCode;
   }

   // 관리자에서 계정 삭제
   @GetMapping(value = "/member/delete/{id}")
   public String memberDelete(@PathVariable("id") Integer id) {
      Member member = this.adminService.getMember(id);
      this.adminService.deleteMember(member);
      return "redirect:/admin/userManage";
   }

   //2023.03.28 프로젝트 상세 보기
   //2023.04.01 이미지 포함해서 출력하기
   @GetMapping(value = "/item/detail/{id}")
   public String detail(Model model, @PathVariable("id") Long id) {
      //이미지 빼고 불러오기
//      Item item = this.adminService.getItemDetail(id);
//      model.addAttribute("item", item);
      ItemFormDto itemFormDto = adminService.getItemDetailNew(id);
        model.addAttribute("item", itemFormDto);
      return "admin/adminItemDetail";
   }

   // 2023.03.29 프로젝트 권한 수정 완료
   @PostMapping("/item/role/modify")
   public @ResponseBody String itemRoleModify(@RequestParam(value = "param1") Long param1,
         @RequestParam(value = "param2") int param2) {

      int pageNum = param1.intValue();

      ItemSellStatus itemRole;

      System.out.println("param1 : " + param1 + ", param2 : " + param2);

      if (param2 == 1) {
         System.out.println("승인까지 넘어옴");
         itemRole = ItemSellStatus.CONFIRM;
         this.adminService.modifyItemRole(param1, itemRole);

         return "/admin/item/detail/" + pageNum;

      } else if (param2 == 2) {
         System.out.println("거절까지 넘어옴");
         itemRole = ItemSellStatus.REFUSE;
         this.adminService.modifyItemRole(param1, itemRole);

         return "/admin/item/detail/" + pageNum;

      } else if (param2 == 3) {
         System.out.println("대기까지 넘어옴");
         itemRole = ItemSellStatus.WAIT;
         this.adminService.modifyItemRole(param1, itemRole);

         return "/admin/item/detail/" + pageNum;

      } else{
         System.out.println("삭제까지 넘어옴");
         this.adminService.deleteItem(param1);

         return "/admin/main";
      }


   }

   // 2023.03.31 판매자의 프로젝트 관리 페이지
   @GetMapping(value = "/seller/main")
   public String adminSellerMain(@RequestParam(value = "value", defaultValue = "1") int param1,
         Model model, Optional<Integer> page, Principal principal) {

      List<ItemSellStatus> aa = new ArrayList(); 
      if(param1 == 1) {
         //대기
         aa.add(ItemSellStatus.WAIT);
      }else if(param1 == 2) {
         //승인
         aa.add(ItemSellStatus.CONFIRM);
      }else if(param1 == 3) {
         //판매
         aa.add(ItemSellStatus.SELL);
      }else if(param1 == 4) {
         //매진
         aa.add(ItemSellStatus.SOLD_OUT);
      }else if(param1 == 5) {
         //마감
         aa.add(ItemSellStatus.CLOSE);
      }else {
         //param1 값 : 6
         //거절
         aa.add(ItemSellStatus.REFUSE);
      }
      
      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("id"));
      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 9, Sort.by(sorts));

      Page<AdminItemDto> itemCondition = this.adminService.getAdminItemPageNew(aa, pageable);

      model.addAttribute("items", itemCondition);
      model.addAttribute("maxPage", 5);
      
      System.out.println("param1 : " + param1);
      
      if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
          Member member = memberService.getMember1(principal.getName());
          model.addAttribute("member", member);   
      }

      return "admin/adminSellerFundingWait";
   }
   
   
   //2023.04.01 판매자 프로젝트 관리 상세 페이지 + 이미지 포함
   @GetMapping(value = "/item/detail/seller/{id}")
   public String detailSeller(Model model, @PathVariable("id") Long id, Principal principal) {
      //이미지 빼고 불러오기
//      Item item = this.adminService.getItemDetail(id);
//      model.addAttribute("item", item);
      
      ItemFormDto itemFormDto = adminService.getItemDetailNew(id);
        model.addAttribute("item", itemFormDto);
        
        if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
            Member member = memberService.getMember1(principal.getName());
            model.addAttribute("member", member);   
        }
        
      return "admin/adminSellerFundingDetail";
   }
   
   //2023.04.01 판매자 프로젝트 권한 관리 수정 완료
      @PostMapping("/item/seller/role/modify")
      public @ResponseBody String itemSellerRoleModify(@RequestParam(value = "param1") Long param1,
            @RequestParam(value = "param2") int param2) {

         int pageNum = param1.intValue();

         ItemSellStatus itemRole;

         System.out.println("param1 : " + param1 + ", param2 : " + param2);

         if (param2 == 1) {
            System.out.println("승인까지 넘어옴");
            itemRole = ItemSellStatus.CONFIRM;
            this.adminService.modifyItemRole(param1, itemRole);

            return "/admin/item/detail/seller/" + pageNum;

         } else if (param2 == 4) {
            System.out.println("삭제까지 넘어옴");
            this.adminService.deleteItem(param1);

            return "/admin/seller/main";
         }else{
            System.out.println("판매까지 넘어옴");
            itemRole = ItemSellStatus.SELL;
            this.adminService.modifyItemRole(param1, itemRole);

            return "/admin/item/detail/seller/" + pageNum;

         }
      }
  
   


}