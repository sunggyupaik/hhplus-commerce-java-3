package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderReader orderReader;

    @Transactional
    public Order getOrder(Long id) {
        return orderReader.getOrder(id);
    }
}
