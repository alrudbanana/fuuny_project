package com.project.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

	//누가 생성했는지
	@CreatedBy
	@Column(updatable = false)
	private String createBy;
	
	//누가 수정 했는지
	@LastModifiedBy
	private String modifiedBy;
	
	
}
