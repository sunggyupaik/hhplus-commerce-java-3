package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderDataPlatformSendService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean send(Order order) {
        try {
            return true;
        } catch (Exception e) {
            log.error("cause = {}, errorMsg = {}", e, e.getMessage());
        }

        return false;
    }
}
