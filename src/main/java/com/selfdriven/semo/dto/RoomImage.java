package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RoomImage {

	 private String imageUrl;
	
	 private int roomId;
	 
	 private LocalDateTime insDate;
	 
	 private LocalDateTime updDate;
}
