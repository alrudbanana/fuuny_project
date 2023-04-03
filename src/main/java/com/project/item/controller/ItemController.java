package com.project.item.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.item.dto.ItemFormDto;
import com.project.item.dto.ItemSearchDto;
import com.project.item.dto.MainItemDto;
import com.project.item.entity.Item;
import com.project.item.repository.ItemRepository;
import com.project.item.service.ItemService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemController {
	
	private final ItemService itemService;
	private final ItemRepository itemRepository;


	
	//상품등록 폼 전달 
	@GetMapping(value = "/saler/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
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
	 
	//상품 수정 -> 정보 출력
	  @GetMapping(value = "/saler/item/{itemId}")
	    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

	        try {
	            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
	            model.addAttribute("itemFormDto", itemFormDto);
	        } catch(EntityNotFoundException e){
	            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
	            model.addAttribute("itemFormDto", new ItemFormDto());
	            return "item/ItemForm";
	        }

	        return "item/ItemForm";
	    }
	 //상품 수정 메소드 
	 @PostMapping(value = "/saler/item/{itemId}")
	    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
	                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
	        if(bindingResult.hasErrors()){
	            return "item/ItemForm";
	        }

	        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
	            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
	            return "item/itemForm";
	        }

	        try {
	            itemService.updateItem(itemFormDto, itemImgFileList);
	        } catch (Exception e){
	            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
	            return "item/itemForm";
	        }

	        return "redirect:/saler/items";
	    }
	 /*
	//메인아이템 불러오기
     @GetMapping(value = "item/itemlist")
     public String main(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
         Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
         Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
         model.addAttribute("items", items);
         model.addAttribute("itemSearchDto", itemSearchDto);
         model.addAttribute("maxPage", 5);

         return "item/mainitmelist";
     }

*/
	 
	 //상품 관리 화면 이동 및 조회한 상품 데이터 화면에 전달 로직 
	 @GetMapping(value = {"/saler/items", "/saler/items/{page}"})
	    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

	        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5); //한 페이지에 보여지는 상품의 수 (0, n) <- 0, 페이지 갯수가 없을수 있어서 / 갯수 조절시 n값 변경  
	        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

	        model.addAttribute("items", items); //조회한 상품 데이터 및 페이징 정보 뷰에 전달 
	        model.addAttribute("itemSearchDto", itemSearchDto); //페이지 전환시 기존 검색조건 유지한체 이동 가능 
	        model.addAttribute("maxPage", 5); //페이지 번호의 최대 갯수 5 개의 이동할 페이지번호만 보여줌 0~4 

	        return "item/itemlist";
	    }
	 
	 //메인페이지에 상품데이터 가져오기
	 @GetMapping(value = "/")
	 public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,6);
		 Page<MainItemDto> items =
				 itemService.getMainItemPage(itemSearchDto, pageable);
		 List<Item> itemList = this.itemRepository.findAll();
		 
		 /* 출력 정보 확인 */ 
		 List<MainItemDto> item = items.getContent(); 
		 for (int i = 0 ; i < item.size() ; i++) {
			 
			 MainItemDto dto = item.get(i); 
			 System.out.println(dto.getItemCategory());
			 System.out.println(dto.getImgUrl());
		 }
		 
		
		 model.addAttribute("items", items);
		 model.addAttribute("itemSearchDto", itemSearchDto);		
		 model.addAttribute("maxPage" , 5);
		 
		 System.out.println("컨트롤러 잘 호출됨 :::: ");
		 
		 return "main";
		 
	 }
	 
		//상세페이지
		@GetMapping(value = "/item/{id}")
		
		public String itemDtl(Model model, @PathVariable("id") Long id) {
		
			ItemFormDto itemFormDto = itemService.getItemDtl(id);
			long remainingDays = itemFormDto.getRemainingDays();
			
		//	List<Item> itemList = this.itemService.getList();
			model.addAttribute("item", itemFormDto);
			model.addAttribute("remainingDays", remainingDays);
			return "item/itemDtl";
		}
		
		
		
		
		
		
	 

	 
	 

		
	    
}
