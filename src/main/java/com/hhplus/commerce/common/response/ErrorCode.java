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

    //point
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 포인트입니다."),
    POINT_BALANCE_OVER(HttpStatus.CONFLICT.value(), "포인트 잔액이 최대를 초과헀습니다."),
    ;
    private final int statusCode;
    private final String errorMsg;
}
