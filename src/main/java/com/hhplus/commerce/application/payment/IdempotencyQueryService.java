package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.domain.payment.PaymentIdempotency;
import com.hhplus.commerce.domain.payment.PaymentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotencyQueryService {
    private final PaymentReader paymentReader;

    public PaymentIdempotency getPaymentIdempotency(Long orderId) {
        return paymentReader.getPaymentIdempotencyWithPessimisticLock(orderId);
    }
}
