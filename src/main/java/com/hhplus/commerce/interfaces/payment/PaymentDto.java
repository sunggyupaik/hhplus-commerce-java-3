package com.hhplus.commerce.interfaces.payment;

import com.hhplus.commerce.domain.payment.PaymentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class PaymentDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "결제 요청")
    public static class PaymentRequest {
        @Schema(description = "주문 식별자", example = "1")
        private Long orderId;

        @Schema(description = "결제 방법", example = "TOSS")
        private String paymentMethod;

        @Schema(description = "결제 금액", example = "1000")
        private Long amount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Schema(description = "결제 응답")
    public static class PayOrderResponse {
        @Schema(description = "결제 식별자", example = "10")
        private Long paymentId;

        @Schema(description = "고객 식별자", example = "10")
        private Long customerId;

        @Schema(description = "주문 식별자", example = "1")
        private Long orderId;

        @Schema(description = "결제 방법", example = "TOSS")
        private String paymentMethod;

        @Schema(description = "결제 금액", example = "1000")
        private Long amount;

        public static PayOrderResponse of(PaymentInfo.PayOrderResponse payment) {
            return PayOrderResponse.builder()
                    .paymentId(payment.getPaymentId())
                    .customerId(payment.getCustomerId())
                    .orderId(payment.getOrderId())
                    .paymentMethod(payment.getPaymentMethod() == null ? null : payment.getPaymentMethod())
                    .amount(payment.getAmount())
                    .build();
        }
    }
}
