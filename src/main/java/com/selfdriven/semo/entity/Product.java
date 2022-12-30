package com.selfdriven.semo.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.*;

@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

	private int productId;

	// TODO : 정규표현식 적용?
	@NotBlank(message="아이디는 필수 입력 값입니다.")
	private String memberId;

	@NotBlank(message= "숙소명은 필수 입력 값입니다. 한글/영문대소문자/숫자/-_ 등을 이용해 2~10자로 입력해주세요.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9-_.,\\s]{2,10}$", message = "객실명은 필수 입력 값입니다. 한글/영문대소문자/숫자/-_ 문자 등을 이용해 2~20자로 입력해주세요.")
	private String productName;

	@NotBlank(message="카테고리는 필수 입력 값입니다.")
	@Pattern(regexp = "^[hmp]$", message = "카테고리에 h, m, p 중 한 글자를 필수로 넣어주세요. (h: hotel, m: motel, p: pension)")
	private String productCategory;

	@Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9-_.,?\"\'\\%\\s\\n]{0,500}$", message = "한글/영문대소문자/숫자/-_ 등을 이용해 500자 내외로 입력해주세요.")
	private String productDescription;

	@NotBlank(message="주소는 필수 입력 값입니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9-_,.\\s]{5,100}$", message = "주소 값은 필수 입력 값입니다. 한글/영문대소문자/숫자/-_ 등을 이용해 5~100자로 입력해주세요.")
	private String address;

	@NotNull
	@Pattern(regexp = "^[가-힣a-zA-Z0-9-_,.\\s]{0,100}$", message = "상세 주소 값은 한글/영문대소문자/숫자/-_ 등을 이용해 100자까지 입력 가능합니다.")
	private String addressDetail;

	@NotBlank(message="우편 번호는 필수 입력 값입니다.")
	@Pattern(regexp = "^[1-7]\\d{5}$", message = "우편 번호 값은 필수 입력 값입니다. 숫자 6글자로 입력해주세요.")
	private String zipCode;

	@NotBlank(message="전화 번호는 필수 입력값입니다.")
	@Pattern(regexp = "^(02|0[3-9]{1}[0-5]{1})(\\d{3}|\\d{4})(\\d{4})$", message = "전화번호는 필수 입력 값입니다. 9 ~ 11 자리의 숫자로 입력해주세요.")
	private String telNum;
	
    private LocalDateTime insDate;

    private LocalDateTime updDate;

	public Product(String memberId, String productName, String productCategory, String address, String addressDetail, String zipCode, String telNum){
		this.memberId = memberId;
		this.productName = productName;
		this.productCategory = productCategory;
		this.address = address;
		this.addressDetail = addressDetail;
		this.zipCode = zipCode;
		this.telNum = telNum;
	}
}
