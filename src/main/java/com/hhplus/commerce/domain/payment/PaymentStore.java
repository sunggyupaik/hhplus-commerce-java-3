package com.hhplus.commerce.domain.payment;

public interface PaymentStore {
    Payment savePayment(Payment payment);

    PaymentHistory saveOrderPaymentHistory(PaymentHistory paymentHistory);
}
