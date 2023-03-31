package com.project.order;

import java.util.ArrayList;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.project.cart.CartOrderDto;
import com.project.entity.Member;
import com.project.item.entity.Item;
import com.project.item.entity.ItemImg;
import com.project.item.repository.ItemImgRepository;
import com.project.item.repository.ItemRepository;
import com.project.order.OrderDto;
import com.project.order.OrderHistDto;
import com.project.order.OrderItemDto;


import com.project.order.Order;
import com.project.order.OrderItem;

import com.project.order.OrderRepository;
import com.project.repository.MemberEmailRepository;
import com.project.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberEmailRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(CartOrderDto cartOrderDto, List<OrderDto> orderDtoList, String email){
    	
    	System.out.println("오더 서비스에서 작동됨");
        Member member = memberRepository.findByEmail(email);
        
        //orderItemList 선언 : Order : order_item 
        List<OrderItem> orderItemList = new ArrayList<>();
        System.out.println("orderItemList 호출됨");
        
        for (OrderDto orderDto : orderDtoList) {
        	
        	System.out.println("For 문 돌아감 :  "+orderDto.getItemId());
        	
        	
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            item.setDonation(cartOrderDto.getDonation());// 후원금 추가 2023-03-29, kk

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            System.out.println("orderItem 값 넣기 확인 : " + orderItem.getDonation());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        System.out.println("오더까지 잘 들어감");
        orderRepository.save(order);

        return order.getId();
    }

}