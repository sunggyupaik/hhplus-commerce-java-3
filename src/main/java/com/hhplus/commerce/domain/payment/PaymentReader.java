package com.hhplus.commerce.domain.payment;

public interface PaymentReader {
    Payment getPayment(Long orderId);

    PaymentIdempotency getPaymentIdempotencyWithPessimisticLock(Long orderId);
}
