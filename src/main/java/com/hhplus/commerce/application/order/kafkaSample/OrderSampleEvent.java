package com.hhplus.commerce.application.order.kafkaSample;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderSampleEvent {
    private Long orderId;

    @Builder
    public OrderSampleEvent(Long orderId) {
        this.orderId = orderId;
    }

    public static OrderSampleEvent of(Long orderId) {
        return OrderSampleEvent.builder()
                .orderId(orderId)
                .build();
    }
}
