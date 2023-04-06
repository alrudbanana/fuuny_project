package com.project.item.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.constant.ItemSellStatus;
import com.project.dto.AdminItemDto;
import com.project.entity.Member;
import com.project.item.dto.ItemFormDto;
import com.project.item.dto.ItemSearchDto;
import com.project.item.entity.Item;
import com.project.item.repository.ItemRepository;
import com.project.item.service.ItemService;
import com.project.service.AdminService;
import com.project.service.MemberService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemController {

private final ItemService itemService;
private final MemberService memberService;
private final ItemRepository itemRepository;
private final AdminService adminService;

	
	//상품등록 폼 전달 
	@GetMapping(value = "/saler/item/new")
    public String itemForm(Model model, Principal principal){
        model.addAttribute("itemFormDto", new ItemFormDto());
        
        if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
            Member member = memberService.getMember1(principal.getName());
            model.addAttribute("member", member);   
        }
        
        
        return "item/itemForm";
	}
	
	//상품 등록 처리 
	 @PostMapping(value = "/saler/item/new")
	    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
	                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Principal principal){

	        if(bindingResult.hasErrors()){
	            return "item/ItemForm";
	        }

	        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
	            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
	            return "item/ItemForm";
	        }

	        try {
	        	itemFormDto.setItemsellstatus(ItemSellStatus.WAIT);
	            itemService.saveItem(itemFormDto, itemImgFileList);
	        } catch (Exception e){
	            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
	            return "item/ItemForm";
	        }
	        
	        if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
	             Member member = memberService.getMember1(principal.getName());
	             model.addAttribute("member", member);   
	         }

	        return "redirect:/";
	    }
	 
	//상품 수정 -> 정보 출력
	  @GetMapping(value = "/saler/item/{itemId}")
	    public String itemDtl(@PathVariable("itemId") Long itemId, Model model, Principal principal){

	        try {
	            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
	            model.addAttribute("itemFormDto", itemFormDto);
	        } catch(EntityNotFoundException e){
	            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
	            model.addAttribute("itemFormDto", new ItemFormDto());
	            return "item/ItemForm";
	        }
	        
	        if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
	             Member member = memberService.getMember1(principal.getName());
	             model.addAttribute("member", member);   
	         }

	        return "item/ItemForm";
	    }
	 //상품 수정 메소드 /23.04.01 프로필이미지 관련 principal추가
	 @PostMapping(value = "/saler/item/{itemId}")
	    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
	                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model, Principal principal ){
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
	        
	        if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
	             Member member = memberService.getMember1(principal.getName());
	             model.addAttribute("member", member);   
	         }

	        return "redirect:/saler/items";
	    }

	 
	 //상품 관리 화면 이동 및 조회한 상품 데이터 화면에 전달 로직 /23.04.01 프로필이미지 관련 principal추가
	 @GetMapping(value = {"/saler/items", "/saler/items/{page}"})
	    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model, Principal principal){

	        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5); //한 페이지에 보여지는 상품의 수 (0, n) <- 0, 페이지 갯수가 없을수 있어서 / 갯수 조절시 n값 변경  
	        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

	        model.addAttribute("items", items); //조회한 상품 데이터 및 페이징 정보 뷰에 전달 
	        model.addAttribute("itemSearchDto", itemSearchDto); //페이지 전환시 기존 검색조건 유지한체 이동 가능 
	        model.addAttribute("maxPage", 5); //페이지 번호의 최대 갯수 5 개의 이동할 페이지번호만 보여줌 0~4 
	        
	        if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
	             Member member = memberService.getMember1(principal.getName());
	             model.addAttribute("member", member);   
	         }

	        return "item/itemlist";
	    }
	 

	 //메인페이지에 상품데이터 가져오기 //23.04.03 프로필 이미지 관련 변수 추가
	 @GetMapping(value = "/")
	 public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal) {
		 List<ItemSellStatus> aa = new ArrayList();
	      aa.add(ItemSellStatus.SELL);
	      aa.add(ItemSellStatus.SOLD_OUT);

	      List<Sort.Order> sorts = new ArrayList<>();
	      sorts.add(Sort.Order.desc("id"));
	      Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 20, Sort.by(sorts));

	      Page<AdminItemDto> itemCondition = this.adminService.getAdminItemPageNew(aa, pageable);
		
		 model.addAttribute("items", itemCondition);
		 model.addAttribute("itemSearchDto", itemSearchDto);		
		 model.addAttribute("maxPage" , 5);
		 

		 if (!(principal == null)) {
	          Member member = memberService.getMember1(principal.getName());
	          model.addAttribute("member", member);   
	      }
	    
		 System.out.println("컨트롤러 잘 호출됨 :::: ");
		 
		 return "main";
		 
	 }
	 
		//상세페이지
		@GetMapping(value = "/item/{id}")
		
		public String itemDtl(Model model, @PathVariable("id") Long id, Principal principal) {
		
			ItemFormDto itemFormDto = itemService.getItemDtl(id);
			long remainingDays = itemFormDto.getRemainingDays();
			
		//	List<Item> itemList = this.itemService.getList();
			model.addAttribute("item", itemFormDto);
			model.addAttribute("remainingDays", remainingDays);
			
			if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
	             Member member = memberService.getMember1(principal.getName());
	             model.addAttribute("member", member);   
	         }
			
			return "item/itemDtl";
		}
		
		
		//카테고리
		@GetMapping("/category/{itemCategory}")
		public String categoryItemList(@RequestParam("itemCategory") String itemCategory, Model model, Principal principal) {
			List<Item> itemList = itemService.getItemByCategory(itemCategory);
			model.addAttribute("itemList", itemList);
			model.addAttribute("itemCategory", itemCategory);
			
			if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
	             Member member = memberService.getMember1(principal.getName());
	             model.addAttribute("member", member);   
	         }
			
			return "categoryItemList";
				}
	 

		
	    
}
