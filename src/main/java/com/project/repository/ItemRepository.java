
package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.item.entity.Item;




@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

	
	//상품 idx로 상품정보 조회 메서드
	Item findByIdx(Long idx);
	
	//List<Item> findByIdx(Long idx);

	List<Item> findByItemName(String itemName);
	List<Item> findByItemDetail(String itemDetail);
	
	//List<Item> findByPriceLessThan(Integer Price);
	
	// List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
}
