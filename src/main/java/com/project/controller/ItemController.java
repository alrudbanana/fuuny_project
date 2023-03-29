package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.dto.ItemFormDto;
import com.project.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;
	
	//펀딩 등록 뷰 페이지 출력
	@GetMapping(value = "/saler/item/new")
	public String itemForm(Model model) {
		model.addAttribute("itemFormDto", new ItemFormDto());
		
		return "item/itemForm";
	}
	
	//이미지 아직 없음
	@PostMapping(value = "/saler/item/new")
	public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResultu,
			Model model) {
		
		if(bindingResultu.hasErrors()) {
			return "item/itemForm";
		}
		
		try {
			itemService.createNewItem(itemFormDto);
		}catch(Exception e) {
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
			return "item/itemForm";
		}
		
		return "redirect:/";
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
