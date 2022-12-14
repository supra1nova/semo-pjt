package com.selfdriven.semo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    
	private int reservationId;

    // id 받아오므로 validation 불필요
	private String memberId;

    // roomId 받아오므로 validation 불필요
    private int roomId;

    // TODO: paymentTerm 카드, 현금, 계좌 이체로 구분?
    @NotBlank
    private String paymentTerm;

    @NotNull
    @PositiveOrZero
    private int totalPrice;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startAt;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endAt;

    // 비워두면 sql에서 자동으로 n 으로 입력되며, 업주가 확인하고 승인하면 y로 변경
    private String status;
    
    private LocalDateTime insDate;
    
    private LocalDateTime updDate;

    public Reservation(String memberId, int roomId, String paymentTerm, int totalPrice, LocalDate startAt, LocalDate endAt){
        this.memberId = memberId;
        this.roomId = roomId;
        this.paymentTerm = paymentTerm;
        this.totalPrice = totalPrice;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
