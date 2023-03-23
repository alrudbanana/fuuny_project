package com.project.dto;

import java.time.LocalDateTime;

import org.springframework.ui.ModelMap;

import com.project.constant.ItemStatus;
import com.project.entity.Item;

import org.modelmapper.ModelMapper;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemFormDto {


	private Long idx; //프로젝트 코드(자동으로늘어나는)
	
	@NotBlank(message = "프로젝트 제목은 필수 입력 값입니다.")
	private String itemName; //프로젝트 명
	

	@NotBlank(message = "카테고리는 필수 선택 값입니다")
	private String itemCategory; //프로젝트 카테고리
	
	
	@NotBlank(message = "프로젝트 상세설명은 필수 입력 값입니다.")
	private String itemDetail; //프로젝트 상세설명
	
	@NotNull(message = "목표 금액은 필수 입력 값입니다.")
	private Integer itemTargetPrice; //목표 금액
	
	@NotNull(message = "상품 금액은 필수 입력 값입니다.")
	private Integer itemPrice; //판매 금액
	
	@NotNull(message = "재고 수량은 필수 입력 값입니다.")
	private Integer itemStockNumber; //재고 수량
	
	/* 프로젝트 일정
	@NotBlank(message = "프로젝트 일정은 필수 선택 값입니다.")
	private LocalDateTime itemStartDate; //프로젝트 시작 날짜
	
	@Column(nullable = false) 
	private LocalDateTime itemEndDate; //프로젝트 마감 날짜
	*/
	
	private ItemStatus itemStatus; //프로젝트 상태(진행중/종료)
	
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//Client form 에서 넘어오는 값을 DTO에 담아서 Item Entity 클래스에 적용후 DB에 저장
	public Item createItem() {
		return modelMapper.map(this, Item.class);
	}
	
	//DB에서 가져온 item Entity 클래스를 DTO 에 주입해서 client 로 전송 하기 위한 매핑 
	public static ItemFormDto of(Item item) {
		return modelMapper.map(item, ItemFormDto.class);
	}
	
}
