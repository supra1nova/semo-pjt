package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ImageProduct {
	
	 private String imageUrl;
	
	 private int productId;
	 
	 private LocalDateTime insDate;
	 
	 private LocalDateTime updDate;

}
