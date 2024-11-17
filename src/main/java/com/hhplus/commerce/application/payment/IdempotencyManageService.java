package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.domain.payment.PaymentInfo;
import com.hhplus.commerce.domain.payment.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdempotencyManageService {
    private final PaymentStore paymentStore;

    @Transactional
    public void saveIdempotencyPayment(
            String idempotencyKey,
            PaymentInfo.PayOrderResponse paymentResponse,
            Long expireMinute
    ) {
        paymentStore.savePaymentIdempotencyRedis(idempotencyKey, paymentResponse, expireMinute);
    }

    @Transactional
    public void deleteIdempotencyPayment(String idempotencyKey) {
        paymentStore.deleteIdempotencyPayment(idempotencyKey);
    }
}
