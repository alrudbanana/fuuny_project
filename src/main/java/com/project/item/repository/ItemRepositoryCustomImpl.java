package com.project.item.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.project.constant.ItemSellStatus;
import com.project.item.dto.ItemSearchDto;
import com.project.item.dto.MainItemDto;
import com.project.item.dto.QMainItemDto;
import com.project.item.entity.Item;
import com.project.item.entity.QItem;
import com.project.item.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

	 private JPAQueryFactory queryFactory; //JPA 쿼리 생성 

		public ItemRepositoryCustomImpl(EntityManager em){
	        this.queryFactory = new JPAQueryFactory(em);
	    }

	    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
	        return searchSellStatus == null ? null : QItem.item.itemsellstatus.eq(searchSellStatus);
	        //return searchSellStatus == null ? null : Item.item.itemSellStatus.eq(searchSellStatus);

	    }

	    private BooleanExpression regDtsAfter(String searchDateType){

	        LocalDateTime dateTime = LocalDateTime.now();
	        //검색어 조회
	        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
	            return null;
	        } else if(StringUtils.equals("1d", searchDateType)){//하루
	            dateTime = dateTime.minusDays(1);
	        } else if(StringUtils.equals("1w", searchDateType)){//일주일 
	            dateTime = dateTime.minusWeeks(1);
	        } else if(StringUtils.equals("1m", searchDateType)){//한달내
	            dateTime = dateTime.minusMonths(1);
	        } else if(StringUtils.equals("6m", searchDateType)){//6달내 
	            dateTime = dateTime.minusMonths(6);
	        }

	        return QItem.item.regTime.after(dateTime); //등록날짜 기준 
	      
	    }
	    //검색어
	    private BooleanExpression searchByLike(String searchBy, String searchQuery){

	        if(StringUtils.equals("itemNm", searchBy)){
	            return QItem.item.itemNm.like("%" + searchQuery + "%");
	        } else if(StringUtils.equals("createdBy", searchBy)){
	            return QItem.item.createBy.like("%" + searchQuery + "%");
	        }

	        return null;
	    }
	    
	    
	    //상품데이터 조회
	    @Override
	    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
	        @SuppressWarnings("deprecation")
			QueryResults<Item> results = queryFactory
	                .selectFrom(QItem.item)
	                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
	                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
	                        searchByLike(itemSearchDto.getSearchBy(),
	                                itemSearchDto.getSearchQuery()))
	                .orderBy(QItem.item.id.desc())
	                .offset(pageable.getOffset())
	                .limit(pageable.getPageSize())
	                .fetchResults();

	        List<Item> content = results.getResults();
	        long total = results.getTotal();
	        return new PageImpl<>(content, pageable, total);
	    }
	    
	  //아이템 Main
	    private BooleanExpression itemNmLike(String searchQuery) {
	        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
	    }

	    @Override
	    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
	        QItem item = QItem.item;
	        QItemImg itemImg = QItemImg.itemImg;

	        List<MainItemDto> content = queryFactory
	                .select(
	                    new QMainItemDto(
	                        item.id,
	                        item.itemNm,
	                        item.itemDetail,
	                        itemImg.imgUrl,
	                        item.price)
	                )
	                .from(itemImg)
	                .join(itemImg.item, item)
	                .where(itemImg.repimgYn.eq("Y"),
	                        itemNmLike(itemSearchDto.getSearchQuery()))
	                .orderBy(item.id.desc())
	                .offset(pageable.getOffset())
	                .limit(pageable.getPageSize())
	                .fetch();

	        Long total = queryFactory
	                .select(item.count())
	                .from(itemImg)
	                .join(itemImg.item, item)
	                .where(itemImg.repimgYn.eq("Y"),
	                        itemNmLike(itemSearchDto.getSearchQuery()))
	                .fetchOne();

	        return new PageImpl<>(content, pageable, total);

	    }


}
