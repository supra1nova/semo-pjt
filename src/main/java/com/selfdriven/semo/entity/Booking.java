package com.selfdriven.semo.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Booking {
	
	 private int year;
	 
	 private int day;
	 
	 private int reserveId;
	 
	 private int roomId;
	 
	 private LocalDateTime insDate;
	 
	 private LocalDateTime updDate;

}
