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
}
