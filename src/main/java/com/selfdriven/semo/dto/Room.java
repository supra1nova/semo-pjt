package com.selfdriven.semo.dto;

import java.time.LocalDateTime;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Room {

	// room id는 db에서 autoincrement로 자동생성-> not null 처리 불필요
	private int roomId;

	@NotNull(message= "객실명은 빈 값으로 넣을 수 없습니다. 다시 한 번 확인해 주세요.")
	private int productId;

	@NotBlank(message= "객실명은 빈 값으로 넣을 수 없습니다. 다시 한 번 확인해 주세요.")
	private String roomName;

	@NotBlank(message= "객실 타입은 빈 값으로 넣을 수 없습니다. 다시 한 번 확인해 주세요.")
	private String roomType;

	private String roomDescription;
    
	private LocalDateTime insDate;
    
	private LocalDateTime updDate;

}
