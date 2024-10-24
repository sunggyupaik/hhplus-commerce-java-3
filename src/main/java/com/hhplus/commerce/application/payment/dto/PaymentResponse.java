package com.hhplus.commerce.application.payment.dto;

import com.hhplus.commerce.domain.payment.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@ToString
@Schema(description = "결제 응답")
public class PaymentResponse {
    @Schema(description = "결제 식별자", example = "10")
    private Long paymentId;

    @Schema(description = "주문 식별자", example = "1")
    private Long orderId;

    @Schema(description = "결제 방법", example = "TOSS")
    private String paymentMethod;

    @Schema(description = "결제 금액", example = "1000")
    private Long amount;

    public static PaymentResponse of(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .paymentMethod(payment.getPaymentMethod() == null? null : payment.getPaymentMethod().name())
                .amount(payment.getAmount())
                .build();
    }
}
