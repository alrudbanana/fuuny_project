package com.project.item.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.item.dto.ItemFormDto;
import com.project.item.entity.Item;
import com.project.item.entity.ItemImg;
import com.project.item.repository.ItemImgRepository;
import com.project.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;



@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImgService itemImgService; //상품 이미지 정보 저장 
	private final ItemImgRepository itemImgRepository;
	
	 //상품 등록 메소드 
	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
       
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
       
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }
}
