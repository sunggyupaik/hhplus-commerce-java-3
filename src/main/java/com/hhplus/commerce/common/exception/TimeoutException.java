package com.hhplus.commerce.common.exception;

import com.hhplus.commerce.common.response.ErrorCode;

public class TimeoutException extends BaseException {
    public TimeoutException(ErrorCode errorCode) {
        super(errorCode);
    }
}
