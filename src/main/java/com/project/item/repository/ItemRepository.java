package com.project.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.item.entity.Item;


public interface ItemRepository extends JpaRepository<Item, Long> {

}
