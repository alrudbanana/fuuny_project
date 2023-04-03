package com.project.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.project.cart.CartOrderDto;


@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private int Donation; //후원금 추가

    private List<CartOrderDto> cartOrderDtoList;
    
}
