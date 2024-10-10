package com.hhplus.commerce.domain.order.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class PaymentResponse {
    @Schema(description = "결제 식별자", example = "10")
    private Long paymentId;

    @Schema(description = "주문 식별자", example = "1")
    private Long orderId;

    @Schema(description = "결제 방법", example = "Toss")
    private String paymentMethod;

    @Schema(description = "결제 금액", example = "1000")
    private Long amount;
}
