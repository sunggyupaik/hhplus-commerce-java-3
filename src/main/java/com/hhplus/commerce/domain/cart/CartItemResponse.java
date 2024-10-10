package com.hhplus.commerce.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@Schema(description = "상품 조회 응답")
public class CartItemResponse {
    @Schema(description = "고객 식별자", example = "1")
    private Long customerId;

    @Schema(description = "상품 식별자", example = "2")
    private Long itemId;

    @Schema(description = "상품 갯수", example = "3")
    private Long quantity;
}
