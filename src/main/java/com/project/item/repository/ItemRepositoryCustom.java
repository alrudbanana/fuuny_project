package com.project.item.repository;


import java.util.List;

import org.springframework.data.domain.Page; //임포트 주의 
import org.springframework.data.domain.Pageable;

import com.project.constant.ItemSellStatus;
import com.project.dto.AdminItemDto;
import com.project.item.dto.ItemSearchDto;
import com.project.item.dto.MainItemDto;
import com.project.item.entity.Item;

public interface ItemRepositoryCustom {
   Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable); //상품 조회 조건을 담고 메소드 
   Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable); //메인아이템 
   
   Page<AdminItemDto> getAdminItemPageNew(List<ItemSellStatus> cond,Pageable pageable);

}