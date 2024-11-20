package com.hhplus.commerce.support.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //common
    COMMON_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "잠시 후 다시 시도해주세요."),
    COMMON_ILLEGAL_STATUS(HttpStatus.BAD_REQUEST, "잘못된 상태값 입니다."),
    COMMON_INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 매개변수값 입니다."),

    //item
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    ITEM_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품 옵션입니다."),
    ITEM_INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품 재고입니다."),
    ITEM_STOCK_INSUFFICIENT(HttpStatus.CONFLICT, "상품의 재고가 부족합니다."),

    //customer
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 고객입니다."),

    //order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    ORDER_DATA_PLATFORM_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "데이터플랫폼 전송 요청 시간초과입니다."),

    //point
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 포인트입니다."),
    POINT_BALANCE_OVER(HttpStatus.CONFLICT, "포인트 잔액이 최대를 초과헀습니다."),
    POINT_BALANCE_INSUFFICIENT(HttpStatus.CONFLICT, "포인트 잔액이 0원 미만입니다."),

    //payment
    PAYMENT_INVALID_PRICE(HttpStatus.BAD_REQUEST, "요청 금액이 잘못되었습니다."),
    PAYMENT_INVALID_CUSTOMER(HttpStatus.BAD_REQUEST, "주문자와 결제자가 다릅니다."),
    PAYMENT_ALREADY_FINISHED(HttpStatus.CONFLICT, "이미 결제가 완료되었습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 결제입니다."),
    PAYMENT_IDEMPOTENCY_KEY_INVALID(HttpStatus.BAD_REQUEST, "멱등키가 존재하지 않습니다."),
    PAYMENT_IDEMPOTENCY_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "요청 본문은 다른데 멱등키는 동일합니다."),
    PAYMENT_ALREADY_PROCESSING(HttpStatus.CONFLICT, "이미 결제 요청을 처리중입니다."),
    PAYMENT_IDEMPOTENCY_NULL(HttpStatus.NOT_FOUND, "존재하지 않은 멱등키 정보입니다."),

    //redisson
    LOCK_NOT_AVAILABLE(HttpStatus.CONFLICT, "락을 획득할 수 없습니다."),
    LOCK_INTERRUPTED_ERROR(HttpStatus.CONFLICT, "인터럽트로 락을 획득할 수 없습니다."),

    //cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장바구니 입니다."),

    ;
    private final HttpStatus httpStatus;
    private final String errorMsg;
}
