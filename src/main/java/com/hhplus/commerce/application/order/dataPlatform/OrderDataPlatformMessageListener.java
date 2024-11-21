package com.hhplus.commerce.application.order.dataPlatform;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderDataPlatformMessageListener {
    private final KafkaTemplate<String, OrderDataPlatformEvent> kafkaTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void success(OrderDataPlatformEvent event) {
        kafkaTemplate.send(OrderDataPlatformEvent.ORDER_DATA_PLATFORM_EVENT_V1_TOPIC, event);
    }
}
