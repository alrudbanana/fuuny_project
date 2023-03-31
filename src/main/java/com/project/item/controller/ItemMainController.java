package com.project.item.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.item.dto.ItemSearchDto;
import com.project.item.dto.MainItemDto;
import com.project.item.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemMainController {

	private final ItemService itemService;
	
	//판매자 메인 페이지 하단 리스트 로직 처리
	@GetMapping(value= "/saler/main")
	public String itemMain(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 6);
		
		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
		
		model.addAttribute("items", items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "item/itemMain";
		
	}
}

