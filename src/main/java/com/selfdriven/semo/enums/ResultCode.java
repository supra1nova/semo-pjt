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
    CANNOT_UPLOAD_IMAGE(1009, "이미지 업로드에 문제가 있습니다. 업체/객실 정보 또는 파일이 유효한 값인지 확인해주세요"),
    CANNOT_DELETE_IMAGE(1010, "이미지 삭제에 문제가 있습니다. 업체/객실 정보 또는 파일이 유효한지 확인해주세요."),
    CANNOT_LOAD_IMAGE_URL(1011, "이미지를 불러올 수 없습니다. 업체/객실 정보 또는 파일이 유효한지 확인해주세요."),
    INVALID_START_DATE_ERROR(1012, "입실 날짜는 현재 날짜보다 과거일 수 없습니다. 다시 한 번 확인해주세요."),
    INVALID_FINISH_DATE_ERROR(1013, "퇴실 날짜는 입실 날짜로부터 최소 하루의 차이가 있어야합니다."),
    INVALID_DATE_ERROR(1014, "해당 객실의 선택된 날짜는 이미 예약이 되었거나, 예약 진행중입니다. 다시 한 번 확인해주세요."),
    DATE_ALREADY_BOOKED(1015, "이미 확정된 예약입니다. 확정 예약 내역에서 취소해주세요."),

    INTERNAL_SERVER_ERROR(199, "서버에 문제가 있습니다.");

    private final int code;
    private final String message;
}
