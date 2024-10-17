package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDataPlatformSendService {
    @Transactional
    public boolean send(Order order) {
        return true;
    }
}
