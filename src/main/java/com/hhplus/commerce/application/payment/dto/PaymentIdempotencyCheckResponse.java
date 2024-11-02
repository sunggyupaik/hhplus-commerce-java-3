package com.hhplus.commerce.application.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "멱등성 키 검사 응답")
public class PaymentIdempotencyCheckResponse {
    @Schema(description = "멱등성 키 존재 여부", example = "true")
    private boolean idempotencyKeyExists;

    @Schema(description = "결제 응답")
    private PaymentResponse payment;

    public static PaymentIdempotencyCheckResponse from(boolean idempotencyKeyExists, PaymentResponse paymentResponse) {
        return PaymentIdempotencyCheckResponse.builder()
                .idempotencyKeyExists(idempotencyKeyExists)
                .payment(paymentResponse)
                .build();
    }
}
