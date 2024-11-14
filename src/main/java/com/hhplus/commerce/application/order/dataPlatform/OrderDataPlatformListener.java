package com.hhplus.commerce.application.order.dataPlatform;

import com.hhplus.commerce.application.order.OrderDataPlatformManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderDataPlatformListener {
    private final OrderDataPlatformManageService orderDataPlatformManageService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void orderDataPlatformHandler(OrderDataPlatformEvent event) {
        OrderDataPlatformPayload orderDataPlatformPayload = OrderDataPlatformPayload.of(event);
        orderDataPlatformManageService.send(orderDataPlatformPayload);
    }
}
