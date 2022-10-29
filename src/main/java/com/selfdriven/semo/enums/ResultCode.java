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
    UNREGISTERED_MEMBER(1008, "가입되지 않은 사용자입니다."),
    CANNOT_IMAGE_UPLOAD(1009, "이미지 업로드에 문제가 있습니다. 업체 및 객실 정보 또는 파일이 유효한 값인지 확인해주세요"),
    INTERNAL_SERVER_ERROR(199, "서버에 문제가 있습니다.");


    private final int code;
    private final String message;
}
