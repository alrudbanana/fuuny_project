package com.project.item.repository;

import java.awt.print.Pageable;
import org.springframework.data.domain.Page;
import com.project.item.dto.ItemSearchDto;
import com.project.item.entity.Item;

public interface ItemRepositoryCustom {
	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
