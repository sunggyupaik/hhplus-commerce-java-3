package com.hhplus.commerce.application.order.dataPlatform;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.outbox.OutBoxCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDataPlatformEvent {
    public static final String ORDER_DOMAIN_NAME = "order";
    public static final String ORDER_DATA_PLATFORM_EVENT_V1_TOPIC = "order-data-platform-event-v1";
    public static final String ORDER_DATA_PLATFORM_EVENT_TYPE = "orderDataPlatform";
    public static final String ORDER_DATA_PLATFORM_EVENT = "orderDataPlatform";

    private Long orderId;

    @Builder
    public OrderDataPlatformEvent(Long orderId) {
        this.orderId = orderId;
    }

    public static OrderDataPlatformEvent of(Order order) {
        return OrderDataPlatformEvent.builder()
                .orderId(order.getId())
                .build();
    }

    public OutBoxCommand.SaveRequest toSaveRequestCommand() {
        return OutBoxCommand.SaveRequest.builder()
                .domainName(ORDER_DOMAIN_NAME)
                .topic(ORDER_DATA_PLATFORM_EVENT_V1_TOPIC)
                .eventType(ORDER_DATA_PLATFORM_EVENT_TYPE)
                .message(String.valueOf(orderId))
                .processed(false)
                .build();
    }
}
