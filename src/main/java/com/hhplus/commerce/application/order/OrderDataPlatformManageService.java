package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dataPlatform.OrderDataPlatformPayload;
import com.hhplus.commerce.common.exception.TimeoutException;
import com.hhplus.commerce.common.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDataPlatformManageService {
    private final OrderDataPlatformSendService orderDataPlatformSendService;

    public boolean send(OrderDataPlatformPayload payload) {
        try {
            log.info("orderDataPlatformPayload: {}", payload);
            boolean isSuccess = orderDataPlatformSendService.send(payload);
            if (isSuccess) return true;
            throw new TimeoutException(ErrorCode.ORDER_DATA_PLATFORM_TIMEOUT);
        } catch (TimeoutException e) {
            boolean isSuccess = orderDataPlatformSendService.send(payload);
            if (isSuccess) return true;
            log.error("DataPlatformSend error, cause = {}, errorMsg = {}", e, e.getMessage());
        }

        throw new TimeoutException(ErrorCode.ORDER_DATA_PLATFORM_TIMEOUT);
    }
}
