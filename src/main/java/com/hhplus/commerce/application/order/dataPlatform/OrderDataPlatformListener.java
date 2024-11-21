package com.hhplus.commerce.application.order.dataPlatform;

import com.hhplus.commerce.application.order.OrderDataPlatformManageService;
import com.hhplus.commerce.domain.order.OrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDataPlatformListener {
    private final OrderDataPlatformManageService orderDataPlatformManageService;

//    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void orderDataPlatformHandler(OrderDataPlatformEvent event) {
        OrderDataPlatformPayload orderDataPlatformPayload = OrderDataPlatformPayload.of(event);
        orderDataPlatformManageService.send(OrderCommand.OrderDataPlatformRequest.of(orderDataPlatformPayload));
    }
}
