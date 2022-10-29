package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
public class ProductImage {
	
	 private String imageUrl;
	
	 private int productId;
	 
	 private LocalDateTime insDate;
	 
	 private LocalDateTime updDate;

}
