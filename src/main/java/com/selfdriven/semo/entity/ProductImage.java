package com.selfdriven.semo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

	@NotBlank(message="이미지 파일명은 필수 값입니다. 다시 한번 확인해주세요.")
	@Pattern(regexp="[a-zA-Z가-힣1-9_-]{10,150}", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.")
	private String imageUrl;

	// 업체 번호 자동으로 받아오므로 굳이 validation 설정 안함.
	private int productId;
	 
	private LocalDateTime insDate;
	 
	private LocalDateTime updDate;

}
