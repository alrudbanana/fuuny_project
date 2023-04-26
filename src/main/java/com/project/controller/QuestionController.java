package com.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;



import com.project.answer.AnswerForm;
import com.project.entity.Member;
import com.project.entity.Question;
import com.project.question.QuestionForm;
import com.project.repository.QuestionRepository;
import com.project.service.MemberService;
import com.project.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController {

   private final QuestionService questionService;
   private final MemberService memberService;

   @GetMapping("/question/list")
   public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
         @RequestParam(value = "kw", defaultValue = "") String kw, Principal principal) {
      Page<Question> paging = this.questionService.getList(page, kw);
      model.addAttribute("paging", paging);
      model.addAttribute("kw", kw);
      
      if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
          Member member = memberService.getMember1(principal.getName());
          model.addAttribute("member", member);   
      }
      
      return "question_list";
   }

   @GetMapping(value = "/question/detail/{id}")
   public String detail(Model model, @PathVariable("id") Long id, AnswerForm answerForm, Principal principal) {

      Question question = this.questionService.getQustion(id);
      model.addAttribute("question", question);
      
      if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
          Member member = memberService.getMember1(principal.getName());
          model.addAttribute("member", member);   
      }
      

      return "question_detail";
   }

   @PreAuthorize("isAuthenticated()")
   @GetMapping("/question/create")
   public String questionCreate(QuestionForm questionForm, Principal principal, Model model) {

	   if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
           Member member = memberService.getMember1(principal.getName());
           model.addAttribute("member", member);   
       }
	   
      return "question_form";
   }

   @PreAuthorize("isAuthenticated()")
   @PostMapping("/question/create")
   public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {

      if (bindingResult.hasErrors()) {

         return "question_form";
      }

      Member member = this.memberService.getMember1(principal.getName());
      this.questionService.create(questionForm.getTitle(), questionForm.getContent(), member , questionForm.getBoardCategory());

      return "redirect:/question/list";
   }

   // 수정
   @PreAuthorize("isAuthenticated()")
   @GetMapping("/question/modify/{id}")
   public String questionModify(QuestionForm questionForm, @PathVariable("id") Long id, Principal principal, Model model) {

      Question question = this.questionService.getQustion(id);
      if (!question.getMember().getEmail().equals(principal.getName())) {

         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
      }
      
      if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
          Member member = memberService.getMember1(principal.getName());
          model.addAttribute("member", member);   
      }

      questionForm.setTitle(question.getTitle());
      questionForm.setContent(question.getContent());
      
      
      return "question_form";
   }

   @PreAuthorize("isAuthenticated()")
   @PostMapping("/question/modify/{id}")
   public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
         @PathVariable("id") Long id, Model model) {

      if (bindingResult.hasErrors()) {
         return "question_form";
      }
      Question question = this.questionService.getQustion(id);
      if (!question.getMember().getEmail().equals(principal.getName())) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
      }
      this.questionService.modify(question, questionForm.getTitle(), questionForm.getContent());
      
      if (!(principal == null)) { //principal에 값이 있으면(로그인상태면)
          Member member = memberService.getMember1(principal.getName());
          model.addAttribute("member", member);   
      }
      
      return String.format("redirect:/question/detail/%s", id);
   }

   // 삭제

   @PreAuthorize("isAuthenticated()")
   @GetMapping("/question/delete/{id}")
   public String questionDelete(Principal principal, @PathVariable("id") Long
         id) {
          Question question = this.questionService.getQustion(id);
          if (!question.getMember().getEmail().equals(principal.getName())) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
          }
          this.questionService.delete(question);
          return "redirect:/question/list";
          }

   
   @GetMapping("/item/kakaopay")
   public String kakao() {
      return "kakaopay";
   }
   
   @GetMapping("/item/cancel")
   public String Cancel() {
      return "cancel";
   }
   
   
   
}