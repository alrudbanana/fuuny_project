package com.project.cart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.project.entity.Member;
import com.project.item.entity.Item;
import com.project.item.repository.ItemRepository;
import com.project.repository.MemberEmailRepository;
import com.project.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final MemberEmailRepository mmr;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
  //  private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        
        Member member = mmr.findByEmail(email);

        //기존의 주문정보가 존재하는지 
        Cart cart = cartRepository.findByMemberIdx(member.getIdx());
        //기존 주문정보가 없으면 cart 테이블에 주문자 정보를 저장 
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        
        // 기존에 장바구니에 등록하는 제품이 cartItem 테이블에 존재하는지 확인         
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){   //기존에 제품이 장바구니에 존재하면 count (갯수)만 update
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {		// 기존에 제품이 장바구니에 존재하지 않으면 전체를 update
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = mmr.findByEmail(email);
        
        Cart cart = cartRepository.findByMemberIdx(member.getIdx());
        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        Member curMember = mmr.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        // curMember.getEmail() : 뷰에서  로그인한 계정을 DB에서 가져옴 
        // savedMember.getEmail() : cartItemID를 조회해서 cart 테이블의 Member의 ID 값 
        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    /*
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
    	
    	//List 선언 
        List<OrderDto> orderDtoList = new ArrayList<>();

        //Client 에서 넘오는 리스트안에 저장된 CertItemID를 끄집어내서        
        
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                            .findById(cartOrderDto.getCartItemId())
                            .orElseThrow(EntityNotFoundException::new);

            //장바구니 아이템 테이블의 정보를 끄집어 내서 order 테이블에 저장하기위해서 
            // orderDto 제품번호, 주문한 갯수 정보만 넣어서 orderDtoList에 저장 
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
          orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }
        */

        /*
        
        //cert_item 테이블의 값을 order, order_item에 값을 Insert 
       Long orderId = orderService.orders(orderDtoList, email);
        
        // cert_item 테이블의 선택된 itemId의 대해서 제거 
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                            .findById(cartOrderDto.getCartItemId())
                            .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }
    */

}
