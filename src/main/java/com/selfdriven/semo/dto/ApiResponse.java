package com.selfdriven.semo.dto;

import com.selfdriven.semo.enums.ResultCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiResponse<T> {
    private int resultCode;
    private String message;
    private T data;

    public static ApiResponse ok() {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResultCode(ResultCode.OK.getCode());
        apiResponse.setMessage(ResultCode.OK.getMessage());
        return apiResponse;
    }

    public static<T> ApiResponse<T> ok(T data) {
        ApiResponse<T> apiResponse = ok();
        apiResponse.setData(data);
        return apiResponse;
    }

    public static<T> ApiResponse fail(int code, String message) {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResultCode(code);
        apiResponse.setMessage(message);
        return apiResponse;
    }
}
