package com.selfdriven.semo.controller.api.advice;

import com.selfdriven.semo.dto.ApiResponse;
import com.selfdriven.semo.enums.ResultCode;
import com.selfdriven.semo.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse apiException(ApiException e){
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ApiResponse.fail(ResultCode.INVALID_PARAMETER.getCode(), ResultCode.INVALID_PARAMETER.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ApiResponse.fail(ResultCode.INVALID_PARAMETER.getCode(), ResultCode.INVALID_PARAMETER.getMessage());
    }

//    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse runtimeException(RuntimeException e) {
        return ApiResponse.fail(ResultCode.UNKNOWN_ERROR.getCode(), ResultCode.UNKNOWN_ERROR.getMessage());
    }


}
