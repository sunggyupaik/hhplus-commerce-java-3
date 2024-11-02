package com.hhplus.commerce.domain.payment;

public interface PaymentReader {
    Payment getPaymentWithPessimisticLock(Long orderId);

    PaymentIdempotency getPaymentIdempotencyWithPessimisticLock(Long orderId, String idempotencyKey);

    PaymentIdempotency getPaymentIdempotency(Long orderId, String idempotencyKey);

    boolean exists(Long orderId, String idempotencyKey);
}
