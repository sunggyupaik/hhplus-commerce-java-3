package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusChangeService {
    @Transactional
    public void changeToComplete(Order order) {
        order.changeToOrderComplete();
    }
}
