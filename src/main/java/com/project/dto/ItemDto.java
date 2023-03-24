package com.project.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemDto {
	
	private Long idx; //프로젝트 코드(자동으로늘어나는)
	

	private String itemName; //프로젝트 명
	

	private String itemCategory; //프로젝트 카테고리
	

	private String itemDetail; //프로젝트 상세설명
	

	private Integer itemTargetPrice; //목표 금액
	

	private Integer itemPrice; //판매 금액
	

	private Integer itemStockNumber; //재고 수량
	
	
	private LocalDateTime itemStartDate; //프로젝트 시작 날짜
	
	
	private LocalDateTime itemEndDate; //프로젝트 마감 날짜



}
