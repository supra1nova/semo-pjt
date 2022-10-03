package com.selfdriven.semo.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RentInfo {
    
	private int rentId;
    
	private int roomId;

    private int price;

    private LocalDateTime startAt;

    private LocalDateTime endAt;



}
