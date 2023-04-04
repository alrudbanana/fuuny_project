package com.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.DataNotFoundException;

import com.project.constant.ItemSellStatus;
import com.project.constant.Role;
import com.project.dto.AdminItemDto;
import com.project.entity.Member;
import com.project.entity.Notice;
import com.project.item.dto.ItemFormDto;
import com.project.item.dto.ItemImgDto;
import com.project.item.entity.Item;
import com.project.item.entity.ItemImg;
import com.project.item.repository.ItemImgRepository;
import com.project.item.repository.ItemRepository;
import com.project.repository.AdminRepository;
import com.project.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

   private final AdminRepository adminRepository;
   private final MemberRepository memberRepository;
   private final ItemRepository itemRepository;
   private final ItemImgRepository itemImgRepository;

   // 공지 리스트 불러오기
   public List<Notice> getList() {
      return this.adminRepository.findAll();
   }

   // 2023.03.25 유저 리스트 불러오기 - 페이징 역순 처리
   public Page<Member> getUserList(int page) {
      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("idx"));
      Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));
      return this.memberRepository.findAll(pageable);
   }
   


   // 공지 하나 갖고 오기 수정
   public Notice getNotice(Integer id) {
      Optional<Notice> notice = this.adminRepository.findById(id);

      if (notice.isPresent()) {
         return notice.get();
      } else {
         throw new DataNotFoundException("공지 사항을 찾을 수 없습니다");
      }
   }

   // 공지 쓰기
   public void writeNotice(String title, String content) {
      Notice n = new Notice();
      n.setTitle(title);
      n.setContent(content);
      n.setRegTime(LocalDateTime.now());
      this.adminRepository.save(n);
   }

   // 공지수정
   public void modifyNotice(Notice notice, String title, String content) {
      notice.setTitle(title);
      notice.setContent(content);
      notice.setUpdateTime(LocalDateTime.now());
      this.adminRepository.save(notice);
   }

   // 공지삭제
   public void deleteNotice(Notice notice) {
      this.adminRepository.delete(notice);
   }

   // 공지 페이징 처리
   public Page<Notice> getList(int page) {
      List<Sort.Order> sorts = new ArrayList<>();
      sorts.add(Sort.Order.desc("id"));
      Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
      return this.adminRepository.findAll(pageable);
   }

   // 멤버 하나 갖고 오기
   public Member getMember(Integer id) {
      Optional<Member> member = this.memberRepository.findById((long) id);

      if (member.isPresent()) {
         return member.get();
      } else {
         throw new DataNotFoundException("공지 사항을 찾을 수 없습니다");
      }
   }

   // 2023.03.27 유저 권한 수정 완료
   public void modifyMemberRole(Long idx, Role role) {

      Member member = this.memberRepository.findById(idx).get();
      member.setRole(role);
      this.memberRepository.save(member);
   }

   // 멤버삭제
   public void deleteMember(Member member) {
      this.memberRepository.delete(member);
   }

   // 아이템 불러오기
//   public Page<AdminItemDto> getItemList(int page) {
//      List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("id"));
//      Pageable pageable = PageRequest.of(page, 12, Sort.by(sorts));
//      return this.itemRepository.getAdminItemPageNew(pageable);
//   }

   // 아이템 상세 불러오기
   public Item getItemDetail(Long id) {
      Optional<Item> item = this.itemRepository.findById(id);
      if (item.isPresent()) {
         return item.get();
      } else {
         throw new DataNotFoundException("Item not found");
      }
   }
   
   //2023.04.01 아이템 상세 불러오기 + 이미지 포함
   @Transactional
    public ItemFormDto getItemDetailNew(Long itemId){
       

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        
        for (ItemImg itemImg : itemImgList) {
           
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }     
         
        Item item = itemRepository.findById(itemId)
              .orElseThrow(EntityNotFoundException::new);
        
        
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

   //2023.03.28 enum 타입 불러오기
   @Transactional
   public Page<Item> getItemCondition(List<ItemSellStatus> cond, Pageable pageable) {   
      return this.itemRepository.findByItemsellstatusIn(cond, pageable);
   }
   
   @Transactional
    public Page<AdminItemDto> getAdminItemPageNew(List<ItemSellStatus> cond, Pageable pageable){
        return itemRepository.getAdminItemPageNew(cond,pageable);
    }

   //2023.03.29 프로젝트 권한 수정 완료 
   public void modifyItemRole(Long id, ItemSellStatus role) {

      Item item = this.itemRepository.findById(id).get();
      item.setItemsellstatus(role);
      this.itemRepository.save(item);
   }

   //2023.03.29 프로젝트 삭제 
   //2023.04.01 연관된 이미지까지 삭제
   public void deleteItem(Long id) {
      List<ItemImg> itemImgs = this.itemImgRepository.findByItemId(id);
       this.itemImgRepository.deleteAll(itemImgs);


       Item item = this.itemRepository.findById(id).orElse(null);
       if (item != null) {
           this.itemRepository.delete(item);
       }
   }

}