package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dataPlatform.OrderDataPlatformPayload;
import com.hhplus.commerce.common.exception.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderDataPlatformSendService {
    public boolean send(OrderDataPlatformPayload payload) {
        try {
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
