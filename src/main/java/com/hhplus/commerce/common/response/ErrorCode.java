package com.hhplus.commerce.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //item
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품입니다."),
    ITEM_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품 옵션입니다."),
    ITEM_INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품 재고입니다."),
    ITEM_STOCK_INSUFFICIENT(HttpStatus.CONFLICT.value(), "상품의 재고가 부족합니다.")
    ;

    private final int statusCode;
    private final String errorMsg;
}
