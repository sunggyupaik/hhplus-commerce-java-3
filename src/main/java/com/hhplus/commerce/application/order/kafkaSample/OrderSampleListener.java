package com.hhplus.commerce.application.order.kafkaSample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderSampleListener {
    @KafkaListener(topics = "sample", groupId = "group_1")
    public void orderSampleHandler(OrderSampleEvent event) {
        log.info("kafka message: {}", event);
    }
}
