package com.hhplus.commerce.application.cart.dto;

import com.hhplus.commerce.domain.cart.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장바구니 추가 요청")
public class CartItemRequest {
    @Schema(description = "상품 식별자", example = "1")
    private Long itemId;

    @Schema(description = "상품 옵션 식별자", example = "2")
    private Long itemOptionId;

    @Schema(description = "상품 갯수", example = "5")
    private Long quantity;

    public Cart toEntity(Long customerId) {
        return Cart.builder()
                .customerId(customerId)
                .itemId(itemId)
                .itemOptionId(itemOptionId)
                .quantity(quantity)
                .build();
    }
}
