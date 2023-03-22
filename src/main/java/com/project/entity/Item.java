package com.project.entity;



import java.time.LocalDateTime;

import com.project.constant.ItemStatus;

//import com.project.constant.ItemStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

	@jakarta.persistence.Id
	@Column(name = "idxItem")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idx; //프로젝트 코드(자동으로늘어나는)
	
	@Column(nullable = false) 
	private String itemName; //프로젝트 명
	
	@Column(nullable = false) 
	private String itemCategory; //프로젝트 카테고리
	
	@Column(nullable = false) 
	private String itemDetail; //프로젝트 상세설명
	
	@Column(nullable = false) 
	private int itemTargetPrice; //목표 금액
	
	@Column(nullable = false) 
	private int itemPrice; //판매 금액
	
	@Column(nullable = false) 
	private LocalDateTime itemStartDate; //프로젝트 시작 날짜
	
	@Column(nullable = false) 
	private LocalDateTime itemEndDate; //프로젝트 마감 날짜
	
	@Enumerated(EnumType.STRING)
	private ItemStatus itemStatus; //프로젝트 상태(진행중/종료)

}
