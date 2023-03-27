package com.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.ItemFormDto;
import com.project.entity.Item;
import com.project.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
	
	private final ItemRepository itemRepository;
	
	//추후 아이템이미지 서비스, 레파지토리 빈생성
	
	//프로젝트 등록 테스트
	public Item createNewItem(ItemFormDto itemFormDto) {
		
		Item item = new Item();
		
		item.setItemStatus(itemFormDto.getItemStatus());
		item.setItemName(itemFormDto.getItemName());
		item.setItemCategory(itemFormDto.getItemCategory());
		item.setItemDetail(itemFormDto.getItemDetail());
		item.setItemTargetPrice(itemFormDto.getItemTargetPrice());
		item.setItemPrice(itemFormDto.getItemPrice());
		item.setItemStockNumber(itemFormDto.getItemStockNumber());
		
		itemRepository.save(item);
		
		return item;
	}

	//idx 값으로 상세페이지 아이템 불러오기
		public List<Item> getList() {
			return this.itemRepository.findAll();
		}
		/*
		public Item getItem(Long idx) {
			Optional<Item> item = this.itemRepository.findByIdx(idx);
			if (item.isPresent()) {
				return item.get();
				
			}else {
				 throw new DataNotFoundException("item not found");
			}
		}
		 */
		
	
	

}
