package com.hhplus.commerce.application.order.dataPlatform;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = "orderId")
public class OrderDataPlatformPayload {
    private final Long orderId;

    @Builder
    public OrderDataPlatformPayload(Long orderId) {
        this.orderId = orderId;
    }

    public static OrderDataPlatformPayload of(OrderDataPlatformEvent event) {
        return OrderDataPlatformPayload.builder()
                .orderId(event.getOrderId())
                .build();
    }
}
