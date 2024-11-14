package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentHistory;
import com.hhplus.commerce.domain.payment.PaymentIdempotency;
import com.hhplus.commerce.domain.payment.PaymentStore;
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
    public PaymentHistory saveOrderPaymentHistorySuccess(PaymentHistory paymentHistory) {
        return paymentHistoryRepository.save(paymentHistory);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentHistory saveOrderPaymentHistoryFail(PaymentHistory paymentHistory) {
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

    @Override
    public String deleteIdempotencyPayment(String idempotencyKey) {
        return paymentRedisRepository.delete(idempotencyKey);
    }
}
