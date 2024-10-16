package com.hhplus.commerce.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 엔티티입니다."),

    //item
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품입니다."),
    ITEM_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품 옵션입니다.")
    ;

    private final int statusCode;
    private final String errorMsg;
}
