package com.hhplus.commerce.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //common
    COMMON_ILLEGAL_STATUS(HttpStatus.BAD_REQUEST.value(), "잘못된 상태값입니다."),

    //item
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품입니다."),
    ITEM_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품 옵션입니다."),
    ITEM_INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 상품 재고입니다."),
    ITEM_STOCK_INSUFFICIENT(HttpStatus.CONFLICT.value(), "상품의 재고가 부족합니다."),

    //customer
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 고객입니다."),

    //order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 주문입니다."),

    //point
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 포인트입니다."),
    POINT_BALANCE_OVER(HttpStatus.CONFLICT.value(), "포인트 잔액이 최대를 초과헀습니다."),
    POINT_BALANCE_INSUFFICIENT(HttpStatus.CONFLICT.value(), "포인트 잔액이 0원 미만입니다."),

    //payment
    PAYMENT_INVALID_PRICE(HttpStatus.BAD_REQUEST.value(), "요청 금액이 잘못되었습니다."),
    PAYMENT_INVALID_CUSTOMER(HttpStatus.BAD_REQUEST.value(), "주문자와 결제자가 다릅니다."),
    PAYMENT_ALREADY_FINISHED(HttpStatus.CONFLICT.value(), "이미 결제가 완료되었습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.CONFLICT.value(), "존재하지 않는 결제입니다."),
    PAYMENT_IDEMPOTENCY_KEY_INVALID(HttpStatus.BAD_REQUEST.value(), "멱등키가 존재하지 않습니다."),
    PAYMENT_IDEMPOTENCY_INVALID(HttpStatus.UNPROCESSABLE_ENTITY.value(), "요청 본문은 다른데 멱등키는 동일합니다."),
    PAYMENT_ALREADY_PROCESSING(HttpStatus.CONFLICT.value(), "이미 결제 요청을 처리중입니다."),
    PAYMENT_IDEMPOTENCY_NULL(HttpStatus.NOT_FOUND.value(), "존재하지 않은 멱등키 정보입니다."),

    //cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 장바구니 입니다."),

    ;
    private final int statusCode;
    private final String errorMsg;
}
