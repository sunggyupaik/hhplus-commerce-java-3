package com.hhplus.commerce.application.payment.dto;

import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentHistory;
import com.hhplus.commerce.domain.payment.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@ToString
@Schema(description = "결제 요청")
public class PaymentRequest {
    @Schema(description = "주문 식별자", example = "1")
    private Long orderId;

    @Schema(description = "고객 식별자", example = "1")
    private Long customerId;

    @Schema(description = "결제 방법", example = "Toss")
    private String paymentMethod;

    @Schema(description = "결제 금액", example = "1000")
    private Long amount;

    @Schema(description = "결제 멱등성 키", example = "12345")
    private String idempotencyKey;

    public Payment toEntity() {
        return Payment.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .amount(amount)
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

    public void addCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void addIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
}
