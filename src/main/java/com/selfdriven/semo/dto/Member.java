package com.selfdriven.semo.dto;


import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder    // builder 패턴 적용
public class Member {
    @NotBlank(message="아이디는 필수 입력 값입니다.")
    @Pattern(regexp="[a-zA-Z1-9_-]{6,256}", message = "아이디는 필수 자동 입력 값으로 영어와 숫자 포함 6~256자 이내의 값입니다.")
    private String memberId;

    @NotBlank(message= "memberType 에 a, c, u 중 한 글자를 필수로 넣어주세요. (a: admin, c: customer, u: user)")
    @Size(min = 1, max = 1, message = "memberType 에 a, c, u 중 한 글자를 필수로 넣어주세요. (a: admin, c: customer, u: user)")
    @Pattern(regexp = "[acu]{1}/g", message = "memberType 에 a, c, u 중 한 글자를 필수로 넣어주세요. (a: admin, c: customer, u: user)")
    private Character memberType;

    @NotBlank(message= "socialType 에 g, k, n 중 한 글자를 필수로 넣어주세요. (g: google, k: kakao, n: naver)")
    @Size(min = 1, max = 1, message = "socialType 에 g, k, n 중 한 글자를 필수로 넣어주세요. (g: google, k: kakao, n: naver)")
    @Pattern(regexp = "[gkn]{1}/g", message = "socialType 에 g, k, n 중 한 글자를 필수로 넣어주세요. (g: google, k: kakao, n: naver)")
    private Character socialType;

    @NotBlank(message="이메일은 필수 입력 값입니다.")
    @Email(message = "입력된 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message="이름은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 필수 입력 값입니다. 한글 2~10자 이내로 입력해주세요.")
    @Size(min=2, max=10, message="올바른 이름을 입력해주세요")
    private String name;

    @NotBlank(message="핸드폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
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

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", memberType=" + memberType +
                ", socialType=" + socialType +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phNum='" + phNum + '\'' +
                ", insDate=" + insDate +
                ", updDate=" + updDate +
                '}';
    }
}
