package com.project.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
	
	private Long idx; //프로젝트 코드(자동으로늘어나는)
	

	private String itemName; //프로젝트 명
	

	private String itemCategory; //프로젝트 카테고리
	

	private String itemDetail; //프로젝트 상세설명
	
	
	
    @QueryProjection
	public MainItemDto (Long idx, String itemName, String itemCategory, 
			String itemDetail,	String itemTargetPrice) {
		
		this.idx = idx;
		this.itemName = itemName;
		this.itemCategory = itemCategory;
		this.itemDetail = itemDetail;
	
		
	}

}
