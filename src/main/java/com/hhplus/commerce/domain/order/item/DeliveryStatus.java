package com.hhplus.commerce.domain.order.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    BEFORE_DELIVERY("배송전"),
    DELIVERY_PREPARE("배송준비중"),
    DELIVERING("배송중"),
    COMPLETE_DELIVERY("배송완료");

    private final String description;
}
