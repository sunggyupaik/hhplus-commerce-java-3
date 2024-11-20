package com.hhplus.commerce.domain.payment;

import com.hhplus.commerce.support.response.ErrorCode;
import com.hhplus.commerce.domain.payment.history.PaymentHistory;
import com.hhplus.commerce.domain.payment.idempotency.PaymentIdempotency;
import com.hhplus.commerce.interfaces.payment.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentCommand {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayOrderRequest {
        private Long orderId;
        private Long customerId;
        private String paymentMethod;
        private Long amount;
        private String idempotencyKey;

        public static PayOrderRequest of(PaymentDto.PaymentRequest request, Long customerId, String idempotencyKey) {
            return PayOrderRequest.builder()
                    .orderId(request.getOrderId())
                    .customerId(customerId)
                    .paymentMethod(request.getPaymentMethod())
                    .amount(request.getAmount())
                    .idempotencyKey(idempotencyKey)
                    .build();
        }

        public Payment toEntity() {
            return Payment.builder()
                    .orderId(orderId)
                    .customerId(customerId)
                    .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                    .amount(amount)
                    .build();
        }

        public PaymentIdempotency toPaymentIdempotencyEntity() {
            return PaymentIdempotency.builder()
                    .idempotencyKey(idempotencyKey)
                    .orderId(orderId)
                    .build();
        }

        public PaymentHistory toPaymentHistoryEntity(ErrorCode errorCode) {
            return PaymentHistory.builder()
                    .orderId(orderId)
                    .customerId(customerId)
                    .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                    .amount(amount)
                    .code(errorCode == null ? "SUCCESS" : errorCode.name())
                    .message(errorCode == null ? "SUCCESS" : errorCode.getErrorMsg())
                    .build();
        }
    }
}
