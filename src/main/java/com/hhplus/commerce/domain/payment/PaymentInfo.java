package com.hhplus.commerce.domain.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class PaymentInfo {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class PayOrderResponse {
        private Long paymentId;
        private Long customerId;
        private Long orderId;
        private String paymentMethod;
        private Long amount;

        public static PayOrderResponse of(Payment payment) {
            return PayOrderResponse.builder()
                    .paymentId(payment.getId())
                    .customerId(payment.getCustomerId())
                    .orderId(payment.getOrderId())
                    .paymentMethod(payment.getPaymentMethod() == null? null : payment.getPaymentMethod().name())
                    .amount(payment.getAmount())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "멱등성 키 검사 응답")
    public static class PaymentIdempotencyCheckResponse {
        @Schema(description = "멱등성 키 존재 여부", example = "true")
        private boolean idempotencyKeyExists;

        @Schema(description = "결제 응답")
        private PayOrderResponse paymentResponse;

        public static PaymentIdempotencyCheckResponse from(
                boolean idempotencyKeyExists,
                PayOrderResponse paymentResponse
        ) {
            return PaymentIdempotencyCheckResponse.builder()
                    .idempotencyKeyExists(idempotencyKeyExists)
                    .paymentResponse(paymentResponse)
                    .build();
        }
    }
}
