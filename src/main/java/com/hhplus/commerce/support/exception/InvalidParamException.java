package com.hhplus.commerce.support.exception;

import com.hhplus.commerce.support.response.ErrorCode;

public class InvalidParamException extends BaseException {
    public InvalidParamException(ErrorCode errorCode) {
        super(errorCode);
    }
}
