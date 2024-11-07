package com.hhplus.commerce.domain.payment;

public interface PaymentStore {
    Payment savePayment(Payment payment);

    PaymentHistory saveOrderPaymentHistory(PaymentHistory paymentHistory);

    PaymentIdempotency savePaymentIdempotency(PaymentIdempotency paymentIdempotency);

    Object savePaymentIdempotencyRedis(String key, Object value, Long expireMinute);

    Boolean setIfAbsent(String key, Object value, Long expireMinute);
}
