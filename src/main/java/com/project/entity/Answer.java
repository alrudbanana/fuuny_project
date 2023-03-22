package com.project.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity	
public class Answer extends BaseEntity {
	
	   @Id   //primary key
	   @GeneratedValue (strategy = GenerationType.IDENTITY)   //시퀀스 할당
	   @Column(name = "answerId")
	   private Long id;
	    
	   @Column(columnDefinition = "TEXT") 
	   private String content;
	   
	   @ManyToOne
	   private Question question;
	   
	   @ManyToOne
	   private Member author;
	   
	  
	   

}
