package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.payment.dto.PaymentIdempotencyCheckResponse;
import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.application.payment.dto.PaymentResponse;
import com.hhplus.commerce.common.distributedLock.DistributedLock;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentIdempotency;
import com.hhplus.commerce.domain.payment.PaymentReader;
import com.hhplus.commerce.domain.payment.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotencyCheckService {
    private final PaymentReader paymentReader;
    private final PaymentStore paymentStore;

    @DistributedLock(key = "'idempotencyKey'.concat(':').concat(#paymentRequest.getIdempotencyKey())")
    public PaymentIdempotencyCheckResponse idempotencyCheck(PaymentRequest paymentRequest) {
        //400 Bad Request 멱등키가 존재하지 않을 때
        String requestIdempotencyKey = paymentRequest.getIdempotencyKey();
        if (requestIdempotencyKey == null) {
            throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
        }

        //멱등키 정보가 존재하지 않으면 return하고 결제 진행
        if (!paymentReader.exists(paymentRequest.getOrderId(), requestIdempotencyKey)) {
            PaymentIdempotency newPaymentIdempotency = PaymentIdempotency.of(paymentRequest);
            paymentStore.savePaymentIdempotency(newPaymentIdempotency);

            return PaymentIdempotencyCheckResponse.from(
                    false,
                    PaymentResponse.of(Payment.builder().build())
            );
        }

        //409 Conflict 이미 요청을 처리중인데 동일한 요청을 했을 때
        Payment payment = null;
        try {
            payment = paymentReader.getPayment(paymentRequest.getOrderId());
        } catch (Exception e) {
            throw new InvalidParamException(ErrorCode.PAYMENT_ALREADY_PROCESSING);
        }

        PaymentIdempotency paymentIdempotency = paymentReader.getPaymentIdempotency(
                paymentRequest.getOrderId(), requestIdempotencyKey);

        //422 Unprocessable Entity 재시도 된 요청 본문(payload)이 처음 요청과 다른데 같은 멱등키를 또 사용했을 때
        if (!validateProcessable(requestIdempotencyKey, paymentIdempotency, paymentRequest, payment)) {
            throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
        }

        return PaymentIdempotencyCheckResponse.from(
                true,
                PaymentResponse.of(payment)
        );
    }

    private boolean validateProcessable(
            String idempotencyKey,
            PaymentIdempotency paymentIdempotency,
            PaymentRequest paymentRequest,
            Payment payment
    ) {
        return paymentIdempotency.isIdempotencyKeySame(idempotencyKey)
                && isSame(payment, paymentRequest);
    }

    private boolean isSame(Payment payment, PaymentRequest paymentRequest) {
        return payment.getPaymentMethod().name().equals(paymentRequest.getPaymentMethod())
                && payment.getCustomerId().equals(paymentRequest.getCustomerId())
                && payment.getOrderId().equals(paymentRequest.getOrderId())
                && payment.getAmount().equals(paymentRequest.getAmount());
    }
}
