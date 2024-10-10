package com.hhplus.commerce.domain.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "잔액 충전 요청")
public class CartItemRequest {
    @Schema(description = "상품 식별자", example = "1")
    private Long itemId;

    @Schema(description = "상품 옵션 식별자", example = "2")
    private Long itemOptionId;

    @Schema(description = "상품 갯수", example = "5")
    private Long quantity;
}
