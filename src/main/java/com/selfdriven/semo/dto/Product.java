package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Product {
   
	private int productId;
    
	private String memberId;
    
	private String productName;
    
	private String productCategory;
    
	private String address;
    
	private String addressDetail;
    
	private String zipCode;
    
	private String telNum;
    
	private LocalDateTime insDate;

    private LocalDateTime updDate;
}
