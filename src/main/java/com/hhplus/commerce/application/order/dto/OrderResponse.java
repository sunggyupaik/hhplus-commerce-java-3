package com.hhplus.commerce.application.order.dto;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStatus;
import com.hhplus.commerce.domain.order.address.Address;
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

    @Schema(description = "주문 주소", example = "")
    private Address address;

    @Schema(description = "주문 상태", example = "INIT")
    private OrderStatus status;

    @Schema(description = "주문 총 금액", example = "20000")
    private Long totalPrice;

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .address(order.getAddress())
                .status(order.getStatus())
                .totalPrice(order.calculatePrice())
                .build();
    }
}
