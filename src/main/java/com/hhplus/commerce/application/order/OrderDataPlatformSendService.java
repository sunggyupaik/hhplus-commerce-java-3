package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDataPlatformSendService {
    @Transactional
    public boolean send(OrderResponse orderResponse) {
        return true;
    }
}
