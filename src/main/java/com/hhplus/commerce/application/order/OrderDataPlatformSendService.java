package com.hhplus.commerce.application.order;

import com.hhplus.commerce.common.exception.TimeoutException;
import com.hhplus.commerce.domain.order.OrderCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderDataPlatformSendService {
    public boolean send(OrderCommand.OrderDataPlatformRequest request) {
        try {
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
