package com.hhplus.commerce.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "장바구니 상품 조회 응답")
public class CartItemResponse {
    @Schema(description = "고객 식별자", example = "1")
    private Long customerId;

    @Schema(description = "상품 식별자", example = "1")
    private Long itemId;

    @Schema(description = "상품 이름", example = "겨울 코트")
    private String itemName;

    @Schema(description = "상품 가격", example = "10000")
    private Long itemPrice;

    @Schema(description = "상품 옵션")
    private CartItemOptionResponse cartItemOptionResponse;

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    @Schema(description = "장바구니 상품 옵션 응답")
    public static class CartItemOptionResponse {
        @Schema(description = "상품 옵션 식별자", example = "10")
        private Long itemOptionId;

        @Schema(description = "상품 옵션 사이즈", example = "95")
        private String itemOptionSize;

        @Schema(description = "상품 옵션 색깔", example = "빨강")
        private String itemOptionColor;

        @Schema(description = "상품 옵션 가격", example = "0")
        private Long itemOptionPrice;

        @Schema(description = "상품 갯수", example = "2")
        private Long quantity;
    }
}
