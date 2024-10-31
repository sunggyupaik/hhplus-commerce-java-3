package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.item.ItemStockService;
import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderCancelHandler {
    private final ItemStockService itemStockService;
    private final OrderStatusChangeService orderStatusChangeService;
    private final OrderQueryService orderQueryService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelOrder(Order order, LocalDateTime datetime, int minute) {
        Order savedOrder = orderQueryService.getOrderWithPessimisticLock(order.getId());
        itemStockService.increaseStockWithTime(savedOrder, datetime, minute);
        orderStatusChangeService.changeToCancel(savedOrder);
    }
}
