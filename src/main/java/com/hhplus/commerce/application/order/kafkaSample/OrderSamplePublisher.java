package com.hhplus.commerce.application.order.kafkaSample;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderSamplePublisher {
    private final KafkaTemplate<String, Long> kafkaTemplate;

    public OrderSamplePublisher(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void success(OrderSampleEvent event) {
        kafkaTemplate.send("sample", event.getOrderId());
    }
}
