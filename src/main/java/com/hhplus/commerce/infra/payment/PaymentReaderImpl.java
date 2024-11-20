package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.support.exception.EntityNotFoundException;
import com.hhplus.commerce.support.response.ErrorCode;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.idempotency.PaymentIdempotency;
import com.hhplus.commerce.domain.payment.PaymentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReaderImpl implements PaymentReader {
    private final PaymentRepository paymentRepository;
    private final PaymentIdempotencyRepository paymentIdempotencyRepository;
    private final PaymentRedisRepository paymentRedisRepository;

    @Override
    public PaymentIdempotency getPaymentIdempotency(Long orderId, String idempotencyKey) {
        return paymentIdempotencyRepository.findByOrderIdAndIdempotencyKey(orderId, idempotencyKey)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PAYMENT_IDEMPOTENCY_NULL));
    }

    @Override
    public boolean exists(Long orderId, String idempotencyKey) {
        return paymentIdempotencyRepository.existsByOrderIdAndIdempotencyKey(orderId, idempotencyKey);
    }

    @Override
    public Payment getPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    @Override
    public Object getPaymentByIdempotencyKeyRedis(String key) {
        return paymentRedisRepository.findPaymentResponseByKey(key);
    }
}
