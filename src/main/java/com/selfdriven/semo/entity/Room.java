package com.selfdriven.semo.entity;

import java.time.LocalDateTime;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Room {

	// room id는 db에서 autoincrement로 자동생성-> not null 처리 불필요
	private int roomId;

	// productId 는 받아오는 값이므로 굳이 설정할 필요 없음 -> 향후 삭제 예정
	@NotNull(message= "업체id는 필수 값입니다. 다시 한 번 확인해 주세요.")
	private int productId;

	@NotBlank(message= "객실명은 필수 입력 값입니다. 한글/영문대소문자/숫자/-_ 등을 이용해 2~10자로 입력해주세요.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9-_]{2,10}$", message = "객실명은 필수 입력 값입니다. 한글/영문/-_ 문자를 이용해 2~10자로 입력해주세요.")
	private String roomName;

	@NotBlank(message= "객실 타입은 빈 값으로 넣을 수 없습니다. 다시 한 번 확인해 주세요.")
	@Pattern(regexp = "^(st|sp|dl|ex|su){1}$", message = "객실 유형에 st, sp, dl, ex, su 중 하나의 값을 필수로 입력해주세요. (st: standard room, sp: superior room, dl: deluxe room, ex: executive room, su: suite room)")
	private String roomType;

	@Size(min = 0, max = 200, message = "객실에 대한 설명은 200자를 초과할 수 없습니다. 다시 한번 확인해주세요.")
	private String roomDescription;

	@NotNull(message = "숙박 비용 책정은 필수 입니다. 다시 한 번 확인해 주세요.")
	@PositiveOrZero
	private int roomWeekPrice;

	@NotNull(message = "숙박 비용 책정은 필수 입니다. 다시 한 번 확인해 주세요.")
	@PositiveOrZero
	private int roomWeekendPrice;
    
	private LocalDateTime insDate;
    
	private LocalDateTime updDate;

}
