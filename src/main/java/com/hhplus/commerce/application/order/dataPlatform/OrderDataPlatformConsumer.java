package com.hhplus.commerce.application.order.dataPlatform;

import com.hhplus.commerce.application.order.OrderDataPlatformManageService;
import com.hhplus.commerce.application.order.OrderExternalEventService;
import com.hhplus.commerce.domain.order.OrderCommand;
import com.hhplus.commerce.domain.outbox.OutBoxCommand;
import com.hhplus.commerce.domain.outbox.Outbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDataPlatformConsumer {
    private final OrderDataPlatformManageService orderDataPlatformManageService;
    private final OrderExternalEventService orderExternalEventService;

    @Transactional
    @KafkaListener(topics = OrderDataPlatformEvent.ORDER_DATA_PLATFORM_EVENT_V1_TOPIC, groupId = "group_2")
    public void orderDataPlatformHandler(OrderDataPlatformEvent event) {
        log.info("topic {}, message: {}", OrderDataPlatformEvent.ORDER_DATA_PLATFORM_EVENT_V1_TOPIC, event);
        OrderDataPlatformPayload orderDataPlatformPayload = OrderDataPlatformPayload.of(event);
        orderDataPlatformManageService.send(OrderCommand.OrderDataPlatformRequest.of(orderDataPlatformPayload));

        OutBoxCommand.FindRequest command = OutBoxCommand.FindRequest.of(
                OrderDataPlatformEvent.ORDER_DATA_PLATFORM_EVENT_V1_TOPIC, String.valueOf(event.getOrderId())
        );
        Outbox outbox = orderExternalEventService.findOutbox(command);
        orderExternalEventService.changeToProcessedTrue(outbox);
    }
}
