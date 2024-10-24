package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentIdempotency;
import com.hhplus.commerce.domain.payment.PaymentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReaderImpl implements PaymentReader {
    private final PaymentRepository paymentRepository;
    private final PaymentIdempotencyRepository paymentIdempotencyRepository;

    @Override
    public Payment getPaymentWithPessimisticLock(Long orderId) {
        return paymentRepository.findByIdWithPessimisticLock(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    @Override
    public PaymentIdempotency getPaymentIdempotencyWithPessimisticLock(Long orderId, String idempotencyKey) {
        return paymentIdempotencyRepository.findByOrderIdWithPessimisticLock(orderId, idempotencyKey)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PAYMENT_IDEMPOTENCY_NULL));
    }

    @Override
    public PaymentIdempotency getPaymentIdempotency(Long orderId, String idempotencyKey) {
        return paymentIdempotencyRepository.findByOrderIdAndIdempotencyKey(orderId, idempotencyKey)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PAYMENT_IDEMPOTENCY_NULL));
    }

    @Override
    public boolean exists(Long orderId, String idempotencyKey) {
        return paymentIdempotencyRepository.existsByOrderIdAndIdempotencyKey(orderId, idempotencyKey);
    }
}
