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

import com.project.DataNotFoundException;
import com.project.entity.Notice;
import com.project.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
private final AdminRepository adminRepository;
	
	//공지 리스트 불러오기
	public List<Notice> getList(){
		return this.adminRepository.findAll();
	}
	
	//공지 하나 갖고 오기
	public Notice getNotice(Integer id) {
		Optional<Notice> notice = this.adminRepository.findById(id);
		
		if(notice.isPresent()) {
			return notice.get();
		}else {
			throw new DataNotFoundException("공지 사항을 찾을 수 없습니다");
		}
	}
	
	//공지 쓰기
	public void writeNotice(String title, String content) {
		Notice n = new Notice();
		n.setTitle(title);
		n.setContent(content);
		n.setRegTime(LocalDateTime.now());
		this.adminRepository.save(n);
	}
	
	//공지수정
	public void modifyNotice(Notice notice, String title, String content) {
		notice.setTitle(title);
		notice.setContent(content);
		notice.setUpdateTime(LocalDateTime.now());
		this.adminRepository.save(notice);
	}
	
	
	//공지삭제
	public void deleteNotice(Notice notice) {
		this.adminRepository.delete(notice);
	}
	
	//공지 페이징 처리
	public Page<Notice> getList(int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.adminRepository.findAll(pageable);
	}

}
