package com.hhplus.commerce.support.exception;

import com.hhplus.commerce.support.response.ErrorCode;

public class IllegalStatusException extends BaseException {
    public IllegalStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
