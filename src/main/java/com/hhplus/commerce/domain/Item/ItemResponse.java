package com.hhplus.commerce.domain.Item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@Schema(description = "상품 조회 응답")
public class ItemResponse {
    @Schema(description = "상품 식별자", example = "1")
    private Long itemId;

    @Schema(description = "상품 이름", example = "겨울 코트")
    private String itemName;

    @Schema(description = "상품 가격", example = "10000")
    private Long itemPrice;

    @Schema(description = "상품 옵션")
    ItemOptionResponse itemOptionResponse;

    @Builder
    @AllArgsConstructor
    public static class ItemOptionResponse {
        @Schema(description = "상품 옵션 식별자", example = "10")
        private Long itemOptionId;

        @Schema(description = "상품 옵션 사이즈", example = "95")
        private String itemOptionSize;

        @Schema(description = "상품 옵션 색깔", example = "빨강")
        private String itemOptionColor;

        @Schema(description = "상품 옵션 가격", example = "0")
        private Long itemOptionPrice;

        @Schema(description = "상품 재고", example = "10")
        private Long quantity;
    }
}
