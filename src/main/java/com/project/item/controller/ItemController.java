package com.project.item.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.item.dto.ItemFormDto;
import com.project.item.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemController {
	
private final ItemService itemService;
	
	//상품등록 폼 전달 
	@GetMapping(value = "/saler/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/ItemForm";
	}
	
	//상품 등록 처리 
	 @PostMapping(value = "/saler/item/new")
	    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
	                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

	        if(bindingResult.hasErrors()){
	            return "item/ItemForm";
	        }

	        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
	            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
	            return "item/ItemForm";
	        }

	        try {
	            itemService.saveItem(itemFormDto, itemImgFileList);
	        } catch (Exception e){
	            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
	            return "item/ItemForm";
	        }

	        return "redirect:/";
	    }
}
