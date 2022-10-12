package com.selfdriven.semo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3FileEnum {
	 PRODUCT("productImage/"),
	 ROOM("productImage/");
	
	private final String route;
	 
}
