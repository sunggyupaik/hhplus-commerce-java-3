package com.hhplus.commerce.application.order.dataPlatform;

import com.hhplus.commerce.application.order.OrderExternalEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderDataPlatformRecordListener {
    private final OrderExternalEventService orderExternalEventService;

    // 아웃박스 테이블에 주문완료 이벤트 기록
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void OrderCompletedRecordHandler(OrderDataPlatformEvent event) {
        orderExternalEventService.create(event.toSaveRequestCommand());
    }
}
