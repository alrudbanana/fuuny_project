package com.project.item.dto;

import com.project.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

	
	private String searchDateType;

    private ItemSellStatus searchSellStatus;
    
    //카테고리 추가 
    private String searchCategory;

    private String searchBy;

    
    //조회할 검색어 저장할 변수
    private String searchQuery = "";

}
