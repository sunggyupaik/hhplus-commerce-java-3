package com.hhplus.commerce.common.exception;

import com.hhplus.commerce.common.response.ErrorCode;

public class IllegalStatusException extends BaseException {
    public IllegalStatusException(ErrorCode errorCode) {
        super(errorCode);
    }
}
