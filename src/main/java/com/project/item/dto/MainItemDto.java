package com.project.item.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MainItemDto {
	private Long id;
	private String itemNm;
	private String itemDetail;
	private String imgUrl;
	private Integer price;
	private String itemCategory; //프로젝트 카테고리
	
	@QueryProjection
	public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price, String itemCategory) {
		this.id = id;
		this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.itemCategory = itemCategory; 
	}
	
	
	
	
}

