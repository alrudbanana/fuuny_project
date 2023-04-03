package com.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.project.DataNotFoundException;
import com.project.entity.Answer;
import com.project.entity.Member;
import com.project.entity.Question;
import com.project.repository.QuestionRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

	private final QuestionRepository questionRepository;

	public List<Question> getList() {

		return this.questionRepository.findAll();
	}

	public Question getQustion(Long id) {

		Optional<Question> question = this.questionRepository.findById(id);

		if (question.isPresent()) {

			return question.get();

		} else {

			throw new DataNotFoundException("question not found");
		}
	}

	public void create(String title, String content, Member member , String boardCategory) {
		Question q = new Question();
		q.setTitle(title);
		q.setContent(content);
		q.setMember(member);
		q.setBoardCategory(boardCategory);
		q.setRegTime(LocalDateTime.now());
		this.questionRepository.save(q);
	}

	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("regTime"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Question> spec = search(kw);
		return this.questionRepository.findAll(spec, pageable);
	}

	// 수정
	public void modify(Question question, String title, String content) {

		question.setTitle(title);
		question.setContent(content);
		question.setUpdateTime(LocalDateTime.now());
		this.questionRepository.save(question);
	}

	// 삭제
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}

	// 검색
	private Specification<Question> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복을 제거
				Join<Question, Member> u1 = q.join("member", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, Member> u2 = a.join("member", JoinType.LEFT);
				return cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"), // 내용
						cb.like(u1.get("memName"), "%" + kw + "%"), // 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
						cb.like(u2.get("memName"), "%" + kw + "%")); // 답변 작성자
			}
		};
	}
	
	//마이페이지에서 문의내역 확인
	public Page<Question> myquestionlist(int page , Long Idx){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regTime"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
	return this.questionRepository.findByMemberIdx(Idx , pageable);
	}
}
