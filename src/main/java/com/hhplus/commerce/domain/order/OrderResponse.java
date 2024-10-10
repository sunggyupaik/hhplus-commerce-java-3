package com.hhplus.commerce.domain.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "주문 응답")
public class OrderResponse {
    @Schema(description = "주문 식별자", example = "1")
    private Long orderId;
}
