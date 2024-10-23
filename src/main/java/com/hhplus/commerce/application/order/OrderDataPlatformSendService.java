package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderDataPlatformSendService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean send(Order order) {
        try {
            return true;
        } catch (Exception e) {

        }

        return false;
    }
}
