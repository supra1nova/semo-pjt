package com.selfdriven.semo.dto;


import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder    // builder 패턴 적용
public class Member {
    @NotBlank(message="아이디는 필수 입력 값입니다.")
    @Pattern(regexp="[a-zA-Z1-9_-]{6,256}", message = "아이디는 필수 자동 입력 값으로 영어와 숫자 포함 6~256자 이내의 값입니다.")
    private String memberId;

    // Character 타입의 경우 validation 적용하기 어렵다 -> string으로 진행 -> @NotBlank 사용
    @NotBlank(message= "memberType 에 a, c, m 중 한 글자를 필수로 넣어주세요. (a: admin, c: customer, m: member)")
    @Pattern(regexp = "^[acm]$", message = "memberType 에 a, c, m 중 한 글자를 필수로 넣어주세요. (a: admin, c: customer, m: member)")
    private String memberType;

    @NotBlank(message= "socialType 에 g, k, n 중 한 글자를 필수로 넣어주세요. (g: google, k: kakao, n: naver)")
    @Pattern(regexp = "^[gkn]$", message = "socialType 에 g, k, n 중 한 글자를 필수로 넣어주세요. (g: google, k: kakao, n: naver)")
    private String socialType;

    @NotBlank(message="이메일은 필수 입력 값입니다.")
    @Email(message = "입력된 형식이 올바르지 않습니다.")
    @Size(min=5, max=50, message="이메일 길이는 5~50자로 제한됩니다. 다시 한번 확인해 주세요.")
    private String email;

    @NotBlank(message="이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 필수 입력 값입니다. 한글 2~10자 이내로 입력해주세요.")
    private String name;

    @NotBlank(message="핸드폰 번호는 필수 입력값입니다.")
//    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "핸드폰 번호는 필수 입력 값입니다. 10 ~ 11 자리의 숫자로 입력해주세요.")
    private String phNum;

    private LocalDateTime insDate;

    private LocalDateTime updDate;

    public Member(String memberId, String email, String name, String phNum) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.phNum = phNum;
    }

}
