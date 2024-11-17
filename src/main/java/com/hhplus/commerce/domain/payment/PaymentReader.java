package com.hhplus.commerce.domain.payment;

import com.hhplus.commerce.domain.payment.idempotency.PaymentIdempotency;

public interface PaymentReader {
    PaymentIdempotency getPaymentIdempotency(Long orderId, String idempotencyKey);

    boolean exists(Long orderId, String idempotencyKey);

    Payment getPayment(Long orderId);

    Object getPaymentByIdempotencyKeyRedis(String key);
}
