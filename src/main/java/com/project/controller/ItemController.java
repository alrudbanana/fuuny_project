package com.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.dto.ItemFormDto;
import com.project.entity.Item;
import com.project.repository.ItemRepository;
import com.project.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;
	private final ItemRepository itemRepository;
	
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
	
	//홈
	@RequestMapping(value = "/")
	public String main(Model model) {
		List<Item> itemList = this.itemRepository.findAll();
		model.addAttribute("itemList", itemList);
		
		
		return "main";
	}
	
	
	//상세페이지
	@GetMapping(value = "/item/{idx}")
	
	public String itemDtl(Model model, @PathVariable("idx") Long idx) {
	
		
		List<Item> itemList = this.itemService.getList();
		model.addAttribute("itemList", itemList);
		return "item/itemDtl";
	}
	
	//임시 헤더//
//	@GetMapping(value = "/header")
//	public String header() {
//		return "header";
//	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
