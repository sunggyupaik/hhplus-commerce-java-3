package com.hhplus.commerce.common.exception;

import com.hhplus.commerce.common.response.ErrorCode;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
