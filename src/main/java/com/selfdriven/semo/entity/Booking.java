package com.selfdriven.semo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

	private Integer bookingId;

	@NotBlank
	@Pattern(regexp="[a-zA-Z1-9_-]{6,256}", message = "아이디는 필수 자동 입력 값으로 영어 대소문자와 숫자 포함 6~256자 이내의 값으로 설정해 주세요")
	private String memberId;

	@NotNull
	@PositiveOrZero
	private Integer reservationId;

	@NotNull
	@PositiveOrZero
	private Integer roomId;

	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate startAt;

	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate endAt;

	private LocalDateTime insDate;

	private LocalDateTime updDate;

	public Booking(String memberId, Integer reservationId, Integer roomId, LocalDate startAt, LocalDate endAt){
		this.memberId = memberId;
		this.reservationId = reservationId;
		this.roomId = roomId;
		this.startAt = startAt;
		this.endAt = endAt;
	}

}
