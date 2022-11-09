package com.selfdriven.semo.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Reservation {
    
	private int reserveId;
    
	private String memberId;
    
	private int roomId;

    private String paymentTerm;

    private int totalPrice;

    private int duration;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    
    private String status;
    
    private LocalDateTime insDate;
    
    private LocalDateTime updDate;



}
