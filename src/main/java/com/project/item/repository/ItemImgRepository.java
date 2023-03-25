package com.project.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.item.entity.ItemImg;


public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
	
}
