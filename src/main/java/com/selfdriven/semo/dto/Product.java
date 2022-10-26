package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	private int productId;

	@NotBlank(message="아이디는 필수 입력 값입니다.")
	private String memberId;
	
	@NotBlank(message="숙소이름은 필수 입력 값입니다.")
	private String productName;
	
	@NotBlank(message="카테고리는 필수 입력 값입니다.")
	private String productCategory;
	
	@NotBlank(message="주소는 필수 입력 값입니다.")
	private String address;
	
	private String addressDetail;
	
	private String zipCode;
	
	private String telNum;
	
    private LocalDateTime insDate;

    private LocalDateTime updDate;


}
