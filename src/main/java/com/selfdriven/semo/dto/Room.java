package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Room {
    
	private int roomId;
    
	private int productId;
    
	private String roomName;
    
	private String roomType;
    
	private String roomDescription;
    
	private LocalDateTime insDate;
    
	private LocalDateTime updDate;
   

}
