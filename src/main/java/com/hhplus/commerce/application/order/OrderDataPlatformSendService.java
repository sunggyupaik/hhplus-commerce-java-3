package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderDataPlatformSendService {
    public boolean send(Order order) {
        return true;
    }
}
