package com.project.item.entity;

import java.time.LocalDate;

import com.project.constant.ItemSellStatus;
import com.project.entity.BaseEntity;
import com.project.item.dto.ItemFormDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@Table(name="item")
public class Item extends BaseEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; //아이템 숫자 
	
	@Column(nullable = false, length = 50)
	private String itemNm; //아이템 이름 
	
	@Column(nullable = false)
	private String itemCategory; //아이템 카테고리
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemsellstatus; //아이템 판매현황(진행중/종료)
	
	@Lob
	@Column(nullable = false)
	private String itemDetail; //아이템 상세설명
	
	@Column(name="itemPrice", nullable = false)
	private int price;//가격  - 판매금액
	
	@Column(name="itemTargetPrice", nullable = false)
	private int price2;//가격  - 목표금액
	
	@Column(nullable = false)
	private int stockNumber; //재고 
	
	@Column(nullable = false)
	private LocalDate startDate; //프로젝트 시작 날짜
	
	@Column(nullable = false)
	private LocalDate endDate; //프로젝트 마감 날짜
	
 
	
	//상품 수정 메소드 
	public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemsellstatus = itemFormDto.getItemSellStatus();
    }
}
