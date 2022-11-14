package com.selfdriven.semo.exception;

import com.selfdriven.semo.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {

    private int code;
    private String message;

    public ApiException(ResultCode resultCode){
        code = resultCode.getCode();
        message = resultCode.getMessage();
    }

}
