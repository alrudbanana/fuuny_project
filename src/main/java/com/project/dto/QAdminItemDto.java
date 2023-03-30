package com.project.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.project.constant.ItemSellStatus;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.ConstructorExpression;

@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAdminItemDto extends ConstructorExpression<AdminItemDto> implements Serializable {

	private static final long serialVersionUID = 1L;

	public QAdminItemDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> itemNm,
			com.querydsl.core.types.Expression<String> itemDetail, com.querydsl.core.types.Expression<String> imgUrl,
			com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<String> itemCategory,
			com.querydsl.core.types.Expression<ItemSellStatus> itemsellstatus,
			com.querydsl.core.types.Expression<LocalDateTime> regTime) {
		super(AdminItemDto.class, new Class<?>[] { long.class, String.class, String.class, String.class, int.class,
				String.class, ItemSellStatus.class, LocalDateTime.class }, id, itemNm, itemDetail, imgUrl, price, itemCategory,
				itemsellstatus,regTime);
	}

}
