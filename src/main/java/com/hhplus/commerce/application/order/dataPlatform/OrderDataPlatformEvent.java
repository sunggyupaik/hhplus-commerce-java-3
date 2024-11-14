package com.hhplus.commerce.application.order.dataPlatform;

import com.hhplus.commerce.domain.order.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderDataPlatformEvent {
    private final Long orderId;

    @Builder
    public OrderDataPlatformEvent(Long orderId) {
        this.orderId = orderId;
    }

    public static OrderDataPlatformEvent of(Order order) {
        return OrderDataPlatformEvent.builder()
                .orderId(order.getId())
                .build();
    }
}
