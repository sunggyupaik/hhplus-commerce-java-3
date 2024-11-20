package com.hhplus.commerce.application.order.kafkaSample;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSampleKafkaService {
    private final OrderSamplePublisher orderSamplePublisher;

    public void sample(Long orderId) {
        orderSamplePublisher.success(OrderSampleEvent.of(orderId));
    }
}
