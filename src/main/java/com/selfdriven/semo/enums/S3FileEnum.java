package com.selfdriven.semo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3FileEnum {
	 PRODUCT("product/"),
	 ROOM("room/"),
	 BUCKET_NAME("https://semo-bucket.s3.ap-northeast-2.amazonaws.com/");
	
	private final String route;
	 
}
