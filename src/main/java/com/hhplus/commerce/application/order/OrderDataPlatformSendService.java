package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dataPlatform.OrderDataPlatformPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDataPlatformSendService {
    public boolean send(OrderDataPlatformPayload payload) {
        try {
            log.info("orderDataPlatformPayload: {}", payload);
            return true;
        } catch (Exception e) {
            log.error("DataPlatformSend error, cause = {}, errorMsg = {}", e, e.getMessage());
        }

        return false;
    }
}
