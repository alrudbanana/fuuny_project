package com.project.item.dto;

import com.project.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

	
	private String searchDateType;

    private ItemSellStatus searchSellStatus = ItemSellStatus.SELL;
    
    //조회 시 어떤 유형으로 조회할지 선택 -> 상품명 / 카테고리명
    private String searchBy;

    
    //조회할 검색어 저장할 변수
    private String searchQuery = "";

}
