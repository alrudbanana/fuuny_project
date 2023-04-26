package com.project.item.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.project.constant.ItemSellStatus;
import com.project.item.entity.Item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemFormDto {
	private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;
    
    @NotNull(message = "카테고리는 필수 선택 값입니다.")
    private String itemCategory;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;
    
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price2; //목표가격

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;
    
    @NotNull(message = "프로젝트 시작일은 필수 선택 값입니다.")
	private LocalDate startDate; //프로젝트 시작 날짜
	
    @NotNull(message = "프로젝트 마감일은 필수 선택 값입니다.")
	private LocalDate endDate; //프로젝트 마감 날짜  
  
	

    private ItemSellStatus itemsellstatus;
    //상퓸 저장 후 상품 이미지 정보를 수정하는 리스트 
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    //상품의 이미지 아이디를 저장하는 리스트, 상품이 들어가있지않아 값을 비워놈 
    private List<Long> itemImgIds = new ArrayList<>();
    
    // Dto의 값을 Entity 클래스와 연결해서 자동으로 값이 주입 되도록 함 
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }
    
    
    //펀딩 남은 일자 구하는 메소드
    public long getRemainingDays() {
    	LocalDate today = LocalDate.now();
    	return ChronoUnit.DAYS.between(today, endDate);
    }

}