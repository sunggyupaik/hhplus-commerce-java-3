package com.hhplus.commerce.common.exception;

import com.hhplus.commerce.common.response.ErrorCode;

public class InvalidParamException extends BaseException {
    public InvalidParamException(ErrorCode errorCode) {
        super(errorCode);
    }
}
