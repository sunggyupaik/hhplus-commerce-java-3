package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.domain.payment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentStoreImpl implements PaymentStore {
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PaymentIdempotencyRepository paymentIdempotencyRepository;
    private final PaymentRedisRepository paymentRedisRepository;

    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentHistory saveOrderPaymentHistory(PaymentHistory paymentHistory) {
        return paymentHistoryRepository.save(paymentHistory);
    }

    @Override
    public PaymentIdempotency savePaymentIdempotency(PaymentIdempotency paymentIdempotency) {
        return paymentIdempotencyRepository.save(paymentIdempotency);
    }

    @Override
    public Object savePaymentIdempotencyRedis(String key, Object value, Long expireMinute) {
        return paymentRedisRepository.setKeyValue(key, value, expireMinute);
    }

    @Override
    public Boolean setIfAbsent(String key, Object value, Long expireMinute) {
        return paymentRedisRepository.setIfAbsent(key, value, expireMinute);
    }
}
