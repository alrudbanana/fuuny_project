package com.project.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.item.entity.ItemImg;


public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
	
	//이민창 03/30 추가
	ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
