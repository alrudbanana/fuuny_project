package com.project.item.dto;

import org.modelmapper.ModelMapper;

import com.project.item.entity.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemImgDto {
		//이미지 엔티티 생성 후 Dto 생성 
	   private Long id;

	    private String imgName;

	    private String oriImgName;

	    private String imgUrl;

	    private String repImgYn;

	    private static ModelMapper modelMapper = new ModelMapper();

	    public static ItemImgDto of(ItemImg itemImg) {
	        return modelMapper.map(itemImg,ItemImgDto.class);
	    }
}
