
package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.project.entity.Item;



public interface ItemRepository extends JpaRepository<Item, Long>{

	//List<Item> findByItemNm(String itemNm);
	
	//List<Item> findByPriceLessThan(Integer Price);
	
	//List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
}
