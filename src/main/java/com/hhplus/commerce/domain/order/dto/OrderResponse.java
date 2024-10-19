package com.hhplus.commerce.domain.order.dto;

import com.hhplus.commerce.domain.order.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "주문 응답")
public class OrderResponse {
    @Schema(description = "주문 식별자", example = "1")
    private Long orderId;

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .build();
    }
}
