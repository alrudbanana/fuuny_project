package com.project.order;

import com.project.entity.BaseEntity;
import com.project.item.entity.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

    private int donation; //후원금
    
    
    //주문 상품 객체 생성 
    public static OrderItem createOrderItem(Item item, int count, int donation){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setDonation(donation);
        item.setStockNumber(item.getStockNumber() - count); //재고 감소 
        return orderItem;
    }
    
    //취소 시 stock (+)
    public void cancel() {
        this.getItem().addStock(this.count);
    }
    
    public int getTotalPrice(){
        return (orderPrice * count) + donation;
    }
    
    //후원금 취소 
    public void cancelDonation(int donation) {
        int updatedDonation = this.donation - donation;
        if(updatedDonation < 0) { // 후원금이 음수가 되지 않도록 체크
            updatedDonation = 0;
        }
        this.donation = updatedDonation;
    }
}