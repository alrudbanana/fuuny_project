package com.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.dto.NoticeFormDto;
import com.project.entity.Notice;
import com.project.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
	@GetMapping(value = "/main")
	public String adminMain() {
		return "adminMain";
	}
	
	@GetMapping(value = "/funding")
	public String adminFunding() {
		return "adminfunding";
	}
	
	@GetMapping(value = "/userManage")
	public String adminUserManage() {
		return "adminUserManage";
	}
	
	//공지 관리로 이동
	@GetMapping(value = "/notice")
	public String adminNotice(Model model, @RequestParam(value = "page",
			defaultValue = "0") int page) {
		Page<Notice> paging = this.adminService.getList(page);
		model.addAttribute("paging", paging);
		return "adminNotice";
	}
	
	//공지 디테일로 이동
	@GetMapping(value = "/notice/detail/{id}")
	public String noticeDetail(Model model, @PathVariable("id") Integer id) {
		Notice notice = this.adminService.getNotice(id);
		model.addAttribute("notice", notice);
		return "adminNoticeDetail";
	}
	
	//공지 생성으로 이동
	@GetMapping(value = "/notice/write")
	public String noticeWrite() {
		return "adminNoticeWriteForm";
	}
	
	//공지생성
	@PostMapping(value = "/notice/write")
	public String noticeWrite(@RequestParam String title, @RequestParam String content) {
		this.adminService.writeNotice(title, content);
		return "redirect:/admin/notice";
	}
	
	//공지
	//공지 수정으로 이동
	@GetMapping(value = "/notice/modify/{id}")
	public String noticeModify(Model model, @PathVariable("id") Integer id) {
		Notice notice = this.adminService.getNotice(id);
		model.addAttribute("notice", notice);
		return "adminNoticeModify";
	}
	
	//공지 수정
	@PostMapping(value = "/notice/modify/{id}")
	public String noticeModify(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult,
			Principal principal, @PathVariable("id") Integer id) {
		
		
		if(bindingResult.hasErrors()) {
			return "adminNoticeModify";
		}
		Notice notice = this.adminService.getNotice(id);
		this.adminService.modifyNotice(notice, noticeFormDto.getTitle(), noticeFormDto.getContent());
		
		return String.format("redirect:/admin/notice/detail/%s", id);
	}
	
	//공지 삭제
	@GetMapping(value = "/notice/delete/{id}")
	public String noticeDelete(@PathVariable("id") Integer id) {
		Notice notice = this.adminService.getNotice(id);
		this.adminService.deleteNotice(notice);
		return "redirect:/admin/notice";
	}
	

	

}
