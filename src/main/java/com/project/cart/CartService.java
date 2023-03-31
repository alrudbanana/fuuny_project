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
    
    @Transactional
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
    public List<CartDetailDto> getCartList(String email){ // 회원 이메일 정보를 이용하여 해당 회원의 장바구니 정보를 조회

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = mmr.findByEmail(email);
        
        Cart cart = cartRepository.findByMemberIdx(member.getIdx());
        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId()); 
        				//findCartDetailDtoList 메소드를 호출해서 장바구니에 담긴 상품들의 상세정보를 가져옴
        return cartDetailDtoList; //List 형태로 변환해 전달
    }
    
    
    //삭제 수정 전 해당상품이 로그인한 사용자의 장바구니에 속한 상품인지 확인 하는 기능 
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        Member curMember = mmr.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId) //cartItemRepository 에서 해당 CartItemId에 해당하는 장바구니 상품 정보를 가져옴 
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember(); // 특정 CartItme 의 정보를 가져오고 그 카트가 소유한 회원정보를 조회 

        // curMember.getEmail() : 뷰에서 로그인한 계정을 DB에서 가져옴 
        // savedMember.getEmail() : cartItemID를 조회해서 cart 테이블의 Member의 ID 값 
        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }
    
    //장바구니에 있는 상품의 수량을 업데이트 하는 메소드  
    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }
    
    
    //상품 아이템 삭제
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }


}
