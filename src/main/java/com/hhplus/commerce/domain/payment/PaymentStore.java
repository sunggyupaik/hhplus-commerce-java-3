package com.hhplus.commerce.domain.payment;

import com.hhplus.commerce.domain.payment.history.PaymentHistory;
import com.hhplus.commerce.domain.payment.idempotency.PaymentIdempotency;

public interface PaymentStore {
    Payment savePayment(Payment payment);

    PaymentHistory saveOrderPaymentHistorySuccess(PaymentHistory paymentHistory);

    PaymentHistory saveOrderPaymentHistoryFail(PaymentHistory paymentHistory);

    PaymentIdempotency savePaymentIdempotency(PaymentIdempotency paymentIdempotency);

    Object savePaymentIdempotencyRedis(String key, Object value, Long expireMinute);

    Boolean setIfAbsent(String key, Object value, Long expireMinute);

    String deleteIdempotencyPayment(String idempotencyKey);
}
