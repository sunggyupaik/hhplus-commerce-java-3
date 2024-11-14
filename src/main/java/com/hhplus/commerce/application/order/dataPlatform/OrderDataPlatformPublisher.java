package com.hhplus.commerce.application.order.dataPlatform;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDataPlatformPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(OrderDataPlatformEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
