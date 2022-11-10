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

    @NotBlank
    private String paymentTerm;

    @PositiveOrZero
    private int totalPrice;

    // TODO : startAt과 endAt이 있는데 duration 이 필요할지?
    @PositiveOrZero
    private int duration;

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

    public Reservation(String memberId, int roomId, String paymentTerm, int totalPrice, int duration, LocalDate startAt, LocalDate endAt){
        this.memberId = memberId;
        this.roomId = roomId;
        this.paymentTerm = paymentTerm;
        this.totalPrice = totalPrice;
        this.duration = duration;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
