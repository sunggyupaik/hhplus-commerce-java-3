package com.hhplus.commerce.application.order.dto;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.payment.OrderPayment;
import com.hhplus.commerce.domain.order.payment.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
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

    public OrderPayment toEntity(Order order) {
        return OrderPayment.builder()
                .order(order)
                .customerId(customerId)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .amount(amount)
                .build();
    }
}
