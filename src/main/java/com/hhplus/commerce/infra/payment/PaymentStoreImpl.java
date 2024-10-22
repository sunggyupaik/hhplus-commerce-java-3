package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentHistory;
import com.hhplus.commerce.domain.payment.PaymentIdempotency;
import com.hhplus.commerce.domain.payment.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStoreImpl implements PaymentStore {
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentIdempotencyRepository paymentIdempotencyRepository;

    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public PaymentHistory saveOrderPaymentHistory(PaymentHistory paymentHistory) {
        return paymentHistoryRepository.save(paymentHistory);
    }

    @Override
    public PaymentIdempotency savePaymentIdempotency(PaymentIdempotency paymentIdempotency) {
        return paymentIdempotencyRepository.save(paymentIdempotency);
    }
}
