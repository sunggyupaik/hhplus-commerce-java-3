package com.hhplus.commerce.application.payment.dto;

import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentHistory;
import com.hhplus.commerce.domain.payment.PaymentMethod;
import com.hhplus.commerce.interfaces.payment.PaymentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
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

    public static PaymentRequest of(PaymentDto.PayOrderRequest request, Long customerId, String idempotencyKey) {
        return PaymentRequest.builder()
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
