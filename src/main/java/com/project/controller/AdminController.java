package com.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Role;
import com.project.constant.ItemSellStatus;
import com.project.constant.RoleMemberCode;
import com.project.dto.NoticeFormDto;
import com.project.entity.Member;
import com.project.entity.Notice;
import com.project.item.entity.Item;
import com.project.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	

	@GetMapping(value = "/main")
	public String adminMain(Model model, @RequestParam(value="page", defaultValue="0") int page) {
		Page<Item> paging = this.adminService.getItemList(page);
		model.addAttribute("paging", paging);
		return "admin/adminMain";
	}

	@GetMapping(value = "/funding")
	public String adminFunding(Model model) {
		List<ItemSellStatus> aa = new ArrayList();
		aa.add(ItemSellStatus.SELL);
		aa.add(ItemSellStatus.SOLD_OUT);
		
		System.out.println("메소드 호출전 ");
		List<Item> itemCondition = this.adminService.getItemCondition(aa);
		
		System.out.println("값 잘받아와서 리턴됨 ");
		
		
		model.addAttribute("itemCondition", itemCondition);
		return "admin/adminfunding";
	}
	
	
	@GetMapping(value = "/funding/approve")
	public String adminFundingApprove() {
		return "admin/adminFundingApprove";
	}
	
	@GetMapping(value = "/funding/end")
	public String adminFundingEnd() {
		return "admin/adminFundingEnd";
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


	//2023.03.27 유저 권한 수정 완료
	@PostMapping("/member/modify")
	public String ex04(@RequestParam(value = "param1") Long param1,
			@RequestParam(value = "param2") Role param2) {

		System.out.println("param1 : " + param1 + ", param2 : " + param2);
		
		this.adminService.modifyMemberRole(param1, param2);
		return "redirect:/admin/userManage";
	}
	
	
	

	@ModelAttribute("roleMemberCode")
	public List<RoleMemberCode> roleMemberCode() {
		List<RoleMemberCode> roleMemberCode = new ArrayList<>();
		roleMemberCode.add(new RoleMemberCode("USER", "일반사용자"));
		roleMemberCode.add(new RoleMemberCode("SELLER", "판매자"));
		roleMemberCode.add(new RoleMemberCode("ADMIN", "관리자"));
		return roleMemberCode;
	}
	
	//관리자에서 계정 삭제
	@GetMapping(value = "/member/delete/{id}")
	public String memberDelete(@PathVariable("id") Integer id) {
		Member member = this.adminService.getMember(id);
		this.adminService.deleteMember(member);
		return "redirect:/admin/userManage";
	}
	
	@GetMapping(value = "/item/detail/{id}")
	public String detail(Model model, @PathVariable("id") Long id) {
		Item item = this.adminService.getItemDetail(id);
		model.addAttribute("item", item);
		return "admin/adminItemDetail";
	}

}