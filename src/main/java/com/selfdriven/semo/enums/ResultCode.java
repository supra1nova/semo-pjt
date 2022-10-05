package com.selfdriven.semo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    OK(0, "OK"),

    INVALID_PARAMETER(1001, "유효한 입력값이 아닙니다."),
    NULLABLE_OBJECT_RETURNED(1002, "빈 값이 입력되었습니다."),
    DUPLICATED_USER_NAME(1003, "동일한 사용자가 존재합니다."),
    USER_NOT_FOUND(1004, "사용자가 존재하지 않습니다."),
    INVALID_PASSWORD(1005, "비밀번호가 유효하지 않습니다."),
    UNKNOWN_ERROR(1006, "알 수 없는 에러가 발생했습니다."),
    ACCESS_DENIED(1007, "잘못된 접근입니다."),

    INTERNAL_SERVER_ERROR(199, "서버에 문제가 있습니다.");


    private final int code;
    private final String message;
}
