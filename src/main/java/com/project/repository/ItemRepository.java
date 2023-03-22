package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
