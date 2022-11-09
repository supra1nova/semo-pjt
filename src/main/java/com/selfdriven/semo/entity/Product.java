package com.selfdriven.semo.entity;

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

	// TODO : 정규표현식 적용?
	@NotBlank(message="아이디는 필수 입력 값입니다.")
	private String memberId;

	// TODO : 정규표현식 적용?
	@NotBlank(message="숙소이름은 필수 입력 값입니다.")
	private String productName;

	// TODO : 정규표현식 적용?
	@NotBlank(message="카테고리는 필수 입력 값입니다.")
	private String productCategory;

	// TODO : 정규표현식 적용?
	@NotBlank(message="주소는 필수 입력 값입니다.")
	private String address;

	// TODO : 정규표현식 적용?
	private String addressDetail;

	// TODO : 정규표현식 적용?
	private String zipCode;

	// TODO : 정규표현식 적용?
	private String telNum;
	
    private LocalDateTime insDate;

    private LocalDateTime updDate;


}
