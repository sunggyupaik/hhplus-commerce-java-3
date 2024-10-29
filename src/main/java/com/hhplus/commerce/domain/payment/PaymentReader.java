package com.hhplus.commerce.domain.payment;

public interface PaymentReader {
    PaymentIdempotency getPaymentIdempotencyWithPessimisticLock(Long orderId, String idempotencyKey);

    boolean exists(Long orderId, String idempotencyKey);

    Payment getPayment(Long orderId);
}
