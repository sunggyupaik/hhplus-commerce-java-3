package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentIdempotency;
import com.hhplus.commerce.domain.payment.PaymentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdempotencyCheckService {
    private final PaymentReader paymentReader;

    @Transactional
    public void idempotencyCheck(String idempotencyKey, PaymentRequest paymentRequest) {
        //Bad REQUEST 400
        if (idempotencyKey == null) {
            throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
        }

        //409 Conflict 이전 요청 처리가 아직 진행 중인데 같은 멱등키로 새로운 요청이 올 때
        PaymentIdempotency paymentIdempotency = null;
        try {
            paymentIdempotency = paymentReader.getPaymentIdempotencyWithPessimisticLock(paymentRequest.getOrderId());
            // h2의 lock은 2초, 동시에 여러 요청이 오는 경우 처음 쓰레드만 성공하고 나머지는 실패하기 위해 의도적으로 sleep 설정
            Thread.sleep(5000);
        } catch (Exception e) {
            throw new InvalidParamException(ErrorCode.PAYMENT_ALREADY_PROCESSING);
        }

        if (paymentIdempotency.getIdempotencyKey() != null) {
            Payment payment = paymentReader.getPayment(paymentRequest.getOrderId());

            //422 Unprocessable Entity 재시도 된 요청 본문(payload)이 처음 요청과 다른데 같은 멱등키를 또 사용했을 때
            if (!validateProcessable(idempotencyKey, paymentIdempotency, paymentRequest, payment)) {
                throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
            }
        }
    }

    private boolean validateProcessable(
            String idempotencyKey,
            PaymentIdempotency paymentIdempotency,
            PaymentRequest paymentRequest,
            Payment payment
    ) {
        return idempotencyKey.equals(paymentIdempotency.getIdempotencyKey())
                && isSame(payment, paymentRequest);
    }

    private boolean isSame(Payment payment, PaymentRequest paymentRequest) {
        return payment.getPaymentMethod().name().equals(paymentRequest.getPaymentMethod())
                && payment.getCustomerId().equals(paymentRequest.getCustomerId())
                && payment.getOrderId().equals(paymentRequest.getOrderId())
                && payment.getAmount().equals(paymentRequest.getAmount());
    }
}
