package com.project.item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.project.constant.ItemSellStatus;
import com.project.item.entity.Item;


public interface ItemRepository extends JpaRepository<Item, Long>, 
         QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {


	
	Optional<Item> findById(Long id);

   List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value="select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

    Page<Item> findAll(Pageable pageable);
    
   
    List<Item> findByItemsellstatusIn(List<ItemSellStatus> itemSellStatus);


	Page<Item> findByItemsellstatusIn(List<ItemSellStatus> cond, Pageable pageable);
	
	//카테고리
	List<Item> findByItemCategory(String itemCategory);
}
