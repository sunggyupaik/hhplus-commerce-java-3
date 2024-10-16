package com.hhplus.commerce.common.response;

import com.hhplus.commerce.common.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BaseException.class)
    public CommonResponse handleBaseException(BaseException e) {
        return CommonResponse.fail(e.getMessage(), e.getErrorCode().name());
    }
}
