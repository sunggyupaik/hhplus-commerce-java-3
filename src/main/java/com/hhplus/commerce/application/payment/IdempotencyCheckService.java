package com.hhplus.commerce.application.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentCommand;
import com.hhplus.commerce.domain.payment.PaymentInfo;
import com.hhplus.commerce.domain.payment.PaymentReader;
import com.hhplus.commerce.domain.payment.PaymentStore;
import com.hhplus.commerce.domain.payment.idempotency.PaymentIdempotency;
import com.hhplus.commerce.support.distributedLock.DistributedLock;
import com.hhplus.commerce.support.exception.InvalidParamException;
import com.hhplus.commerce.support.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdempotencyCheckService {
    private final ObjectMapper objectMapper;
    private final PaymentReader paymentReader;
    private final PaymentStore paymentStore;

    @DistributedLock(key = "'idempotencyKey'.concat(':').concat(#payOrderRequest.getOrderId())")
    public PaymentInfo.PaymentIdempotencyCheckResponse idempotencyCheck(PaymentCommand.PayOrderRequest payOrderRequest) {
        //400 Bad Request 멱등키가 존재하지 않을 때
        String requestIdempotencyKey = payOrderRequest.getIdempotencyKey();
        if (requestIdempotencyKey == null) {
            throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
        }

        //멱등키 정보가 존재하지 않으면 return하고 결제 진행
        if (!paymentReader.exists(payOrderRequest.getOrderId(), requestIdempotencyKey)) {
            PaymentIdempotency newPaymentIdempotency = payOrderRequest.toPaymentIdempotencyEntity();
            paymentStore.savePaymentIdempotency(newPaymentIdempotency);

            return PaymentInfo.PaymentIdempotencyCheckResponse.from(
                    false,
                    PaymentInfo.PayOrderResponse.of(Payment.builder().build())
            );
        }

        //409 Conflict 이미 요청을 처리중인데 동일한 요청을 했을 때
        Payment payment = null;
        try {
            payment = paymentReader.getPayment(payOrderRequest.getOrderId());
        } catch (Exception e) {
            throw new InvalidParamException(ErrorCode.PAYMENT_ALREADY_PROCESSING);
        }

        PaymentIdempotency paymentIdempotency = paymentReader.getPaymentIdempotency(
                payOrderRequest.getOrderId(), requestIdempotencyKey);

        //422 Unprocessable Entity 재시도 된 요청 본문(payload)이 처음 요청과 다른데 같은 멱등키를 또 사용했을 때
        if (!validateProcessable(requestIdempotencyKey, paymentIdempotency, payOrderRequest, payment)) {
            throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
        }

        return PaymentInfo.PaymentIdempotencyCheckResponse.from(
                true,
                PaymentInfo.PayOrderResponse.of(payment)
        );
    }

    private boolean validateProcessable(
            String idempotencyKey,
            PaymentIdempotency paymentIdempotency,
            PaymentCommand.PayOrderRequest payOrderRequest,
            Payment payment
    ) {
        return paymentIdempotency.isIdempotencyKeySame(idempotencyKey)
                && isSame(payment, payOrderRequest);
    }

    private boolean isSame(Payment payment, PaymentCommand.PayOrderRequest payOrderRequest) {
        return payment.getPaymentMethod().name().equals(payOrderRequest.getPaymentMethod())
                && payment.getCustomerId().equals(payOrderRequest.getCustomerId())
                && payment.getOrderId().equals(payOrderRequest.getOrderId())
                && payment.getAmount().equals(payOrderRequest.getAmount());
    }

    @Transactional
    public PaymentInfo.PaymentIdempotencyCheckResponse idempotencyCheckRedis(PaymentCommand.PayOrderRequest payOrderRequest) {
        //400 Bad Request 멱등키가 존재하지 않을 때
        String requestIdempotencyKey = payOrderRequest.getIdempotencyKey();
        if (requestIdempotencyKey == null) {
            throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
        }

        // 멱등키에 결과 정보가 존재하지 않으면 결제 진행
        Boolean valueIsAbsent = paymentStore.setIfAbsent(
                requestIdempotencyKey,
                "processing",
                1L
        );

        if (valueIsAbsent.equals(true)) {
            return PaymentInfo.PaymentIdempotencyCheckResponse.from(
                    false,
                    PaymentInfo.PayOrderResponse.of(Payment.empty())
            );
        }

        //409 Conflict 이미 요청을 처리중인데 동일한 요청을 했을 때
        Object value = paymentReader.getPaymentByIdempotencyKeyRedis(requestIdempotencyKey);
        if (value.toString().equals("processing")) {
            throw new InvalidParamException(ErrorCode.PAYMENT_ALREADY_PROCESSING);
        }

        //422 Unprocessable Entity 재시도 된 요청 본문(payload)이 처음 요청과 다른데 같은 멱등키를 또 사용했을 때
        PaymentInfo.PayOrderResponse paymentResponse = objectMapper.convertValue(value, PaymentInfo.PayOrderResponse.class);
        if (!isSame(paymentResponse, payOrderRequest)) {
            throw new InvalidParamException(ErrorCode.PAYMENT_IDEMPOTENCY_KEY_INVALID);
        }

        return PaymentInfo.PaymentIdempotencyCheckResponse.from(
                true,
                paymentResponse
        );
    }

    private boolean isSame(
            PaymentInfo.PayOrderResponse paymentResponse,
            PaymentCommand.PayOrderRequest payOrderRequest
    ) {
        return paymentResponse.getPaymentMethod().equals(payOrderRequest.getPaymentMethod())
                && paymentResponse.getCustomerId().equals(payOrderRequest.getCustomerId())
                && paymentResponse.getOrderId().equals(payOrderRequest.getOrderId())
                && paymentResponse.getAmount().equals(payOrderRequest.getAmount());
    }
}
