package com.project.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.ItemDto;
import com.project.item.dto.ItemFormDto;
import com.project.item.entity.Item;
import com.project.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
	
	@Autowired
	private final ItemRepository itemRepository;
	
	//추후 아이템이미지 서비스, 레파지토리 빈생성
	
	//프로젝트 등록 테스트
	public Item createNewItem(ItemFormDto itemFormDto) {
		
		Item item = new Item();
		
		item.setItemsellstatus(itemFormDto.getItemSellStatus());
		item.setItemNm(itemFormDto.getItemNm());
		item.setItemCategory(itemFormDto.getItemCategory());
		item.setItemDetail(itemFormDto.getItemDetail());
		item.setPrice(itemFormDto.getPrice());
		item.setPrice2(itemFormDto.getPrice2());
		item.setStockNumber(itemFormDto.getStockNumber());
		
		
		itemRepository.save(item);
		
		return item;
	}

	//idx 값으로 상세페이지 아이템 불러오기
	public List<Item> getList() {
		return this.itemRepository.findAll();
	}
	
	public ItemDto convertToDto(Item item) {
		ItemDto itemDto = new ItemDto();
		
		itemDto.setItemName(item.getItemNm());
		itemDto.setItemCategory(item.getItemCategory());
		itemDto.setItemDetail(item.getItemDetail());
		itemDto.setItemTargetPrice(item.getPrice());
		itemDto.setItemPrice(item.getPrice2());
		itemDto.setItemStockNumber(item.getStockNumber());
		
		return itemDto;
		
	}
	
	
	public ItemDto getItemByIdx(Long idx) {
	
	
		//상품 idx로 상품정보 조회
		Item item = itemRepository.findByIdx(idx);
		
		//조죄한 정보를 ItemDto로 변환하여 변환
		ItemDto itemDto = convertToDto(item);
		
		/*
		LocalDate itemEndDate = item.getite; // 상품 마감일
	    LocalDate today = LocalDate.now(); // 오늘 날짜
	    long daysLeft = ChronoUnit.DAYS.between(today, itemEndDate); // 상품 마감일까지 남은 일수

	    itemDto.setDaysLeft(daysLeft); // itemDto 객체의 daysLeft 필드에 남은 일수를 설정
		*/
		return itemDto;
		
	}
		
	

}
