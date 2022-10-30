package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

	@NotBlank(message="이미지 파일명은 필수 값입니다. 다시 한번 확인해주세요.")
	@Pattern(regexp="[a-zA-Z1-9_-]{10,150}", message = "유효하지 않은 이미지 파일명입니다. 다시 한번 확인해주세요.")
	private String imageUrl;
	
	private int productId;
	 
	private LocalDateTime insDate;
	 
	private LocalDateTime updDate;

}
