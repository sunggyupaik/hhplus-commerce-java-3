package com.hhplus.commerce.support.exception;

import com.hhplus.commerce.support.response.ErrorCode;

public class TimeoutException extends BaseException {
    public TimeoutException(ErrorCode errorCode) {
        super(errorCode);
    }
}
