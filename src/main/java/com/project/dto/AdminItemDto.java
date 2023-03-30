package com.project.dto;


import java.time.LocalDateTime;

import com.project.constant.ItemSellStatus;
import com.querydsl.core.annotations.QueryProjection;

import jakarta.persistence.Entity;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
//@AllArgsConstructor
public class AdminItemDto {
	
	private Long id;
	private String itemNm;
	private String itemDetail;
	private String imgUrl;
	private Integer price;
	private ItemSellStatus itemSellStatus;
	private String itemCategory;
	private LocalDateTime regTime;
	
	@QueryProjection
	public AdminItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price,String itemCategory,
			ItemSellStatus itemSellStatus,LocalDateTime regTime) {
		this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.itemSellStatus=itemSellStatus;
        this.itemCategory=itemCategory;
        this.regTime=regTime;
		
	}
	

}
