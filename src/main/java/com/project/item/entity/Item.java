package com.project.item.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.project.constant.ItemSellStatus;
import com.project.entity.BaseEntity;
import com.project.exception.OutOfStockException;
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
	
	private int Donation; // 후원금
	
	
	
 
	
	//상품 수정 메소드 
	public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.itemCategory = itemFormDto.getItemCategory();
        this.price = itemFormDto.getPrice();
        this.price2 = itemFormDto.getPrice2();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemsellstatus = itemFormDto.getItemSellStatus();
        this.startDate = itemFormDto.getStartDate();
        this.endDate = itemFormDto.getEndDate();
    }
	  public void removeStock(int stockNumber){
	        int restStock = this.stockNumber - stockNumber;
	        if(restStock<0){
	            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
	        }
	        this.stockNumber = restStock;
	    }

	    public void addStock(int stockNumber){
	        this.stockNumber += stockNumber;
	    }
	    
	    
	    //펀딩 남은일자 메소드
	    public long getRemainingDays() {
	    	LocalDate today = LocalDate.now();
	    	return ChronoUnit.DAYS.between(today, endDate);
	    }
	    
	    
	    
	
}
