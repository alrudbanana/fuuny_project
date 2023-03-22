package com.project.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class ItemImage extends BaseEntity {
	
	@Id
	@Column(name = "idxImgIdx")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idx; //이미지 코드(자동으로늘어나는)

}
