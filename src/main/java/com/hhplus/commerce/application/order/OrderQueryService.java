package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderReader orderReader;

    @Transactional
    public Order getOrder(Long id) {
        return orderReader.getOrder(id);
    }

    @Transactional
    public Order getOrderWithPessimisticLock(Long id) {
        return orderReader.getOrderWithPessimisticLock(id);
    }

    @Transactional
    public List<Order> getInitOrders() {
        return orderReader.getInitOrders();
    }
}
