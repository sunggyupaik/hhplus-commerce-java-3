package com.hhplus.commerce.application.order;

<<<<<<< HEAD
import com.hhplus.commerce.domain.order.Order;
import lombok.extern.slf4j.Slf4j;
=======
import com.hhplus.commerce.application.order.dto.OrderResponse;
>>>>>>> e1c04b89e691ca26f1cf8d4c15233190a736d599
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderDataPlatformSendService {
<<<<<<< HEAD
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean send(Order order) {
        try {
            return true;
        } catch (Exception e) {
            log.error("cause = {}, errorMsg = {}", e, e.getMessage());
        }

        return false;
=======
    @Transactional
    public boolean send(OrderResponse orderResponse) {
        return true;
>>>>>>> e1c04b89e691ca26f1cf8d4c15233190a736d599
    }
}
