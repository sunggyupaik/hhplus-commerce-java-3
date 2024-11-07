package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.payment.dto.PaymentResponse;
import com.hhplus.commerce.domain.payment.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdempotencyManageService {
    private final PaymentStore paymentStore;

    @Transactional
    public void saveIdempotencyPayment(String idempotencyKey, PaymentResponse paymentResponse, Long expireMinute) {
        paymentStore.savePaymentIdempotencyRedis(idempotencyKey, paymentResponse, expireMinute);
    }
}
