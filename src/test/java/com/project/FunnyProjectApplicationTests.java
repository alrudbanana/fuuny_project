package com.project;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.dto.ItemDto;
import com.project.dto.ItemFormDto;
import com.project.entity.Answer;
import com.project.entity.Item;
import com.project.entity.Question;
import com.project.repository.AnswerRepository;
import com.project.repository.ItemRepository;
import com.project.entity.Notice;
import com.project.entity.Question;
import com.project.repository.AdminRepository;

import com.project.repository.QuestionRepository;
import com.project.service.AdminService;
import com.project.service.ItemService;

@SpringBootTest
class FunnyProjectApplicationTests {
	
//	@Autowired
//	private QuestionRepository questionRepository;
//	
//	@Test
//	void testJpa() {
//		Question q1 = new Question();
//		q1.setTitle("Test 입니다.");
//		q1.setContent("Test 내용입니다.");
//		q1.setRegTime(LocalDateTime.now());
//		this.questionRepository.save(q1);	// 첫번째 질문 저장
//		
//		Question q2 = new Question();
//		q2.setTitle("Test2 입니다.");
//		q2.setContent("Test2 내용입니다.");
//		q2.setRegTime(LocalDateTime.now());
//		this.questionRepository.save(q2);	// 첫번째 질문 저장
//	}
//
//	@Test
//	void contextLoads() {
//	}
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private Item itemDto;
	
	
	
//	@Test
//	void testAdmin() {
//		for (int i = 1; i <= 50; i++) {
//            String itemName = String.format("아이템 이름:[%03d]", i);
//            String itemCategory = "---";
//            String itemDetail = String.format("상세설명:[%03d]", i);
//            int itemTargetPrice = i*1000;
//            int itemPrice = i*50;
//            int itemStockNumber = i*2;
//
//            itemDto.setIdx((long) i);
//            itemDto.setItemName(itemName);
//            itemDto.setItemCategory(itemCategory);
//            itemDto.setItemDetail(itemDetail);
//            itemDto.setItemTargetPrice(itemTargetPrice);
//            itemDto.setItemPrice(itemPrice);
//            itemDto.setItemStockNumber(itemStockNumber);
//            this.itemRepository.save(itemDto);
//        }
//
//	}
	


}
