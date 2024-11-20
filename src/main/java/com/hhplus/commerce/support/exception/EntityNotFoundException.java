package com.hhplus.commerce.support.exception;

import com.hhplus.commerce.support.response.ErrorCode;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
