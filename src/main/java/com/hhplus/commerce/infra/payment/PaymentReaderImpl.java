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
    public Payment getPayment(Long orderId) {
        return paymentRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    @Override
    public PaymentIdempotency getPaymentIdempotencyWithPessimisticLock(Long orderId) {
        return paymentIdempotencyRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PAYMENT_IDEMPOTENCY_NULL));
    }
}
