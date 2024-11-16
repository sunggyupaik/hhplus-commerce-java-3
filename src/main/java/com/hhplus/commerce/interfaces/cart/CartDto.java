package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.domain.cart.CartInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class CartDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "장바구니 추가 요청")
    public static class AddRequest {
        @Schema(description = "상품 식별자", example = "1")
        private Long itemId;

        @Schema(description = "상품 옵션 식별자", example = "2")
        private Long itemOptionId;

        @Schema(description = "상품 갯수", example = "5")
        private Long quantity;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "장바구니 삭제 요청")
    public static class DeleteRequest {
        @Schema(description = "상품 옵션 식별자 목록", example = "[1, 2, 3]")
        private List<Long> itemOptionIds;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "장바구니 응답")
    public static class DetailResponse {
        @Schema(description = "상품 목록")
        private List<ItemResponse> itemList;

        public static DetailResponse of(CartInfo.DetailResponse cartInfo) {
            return DetailResponse.builder()
                    .itemList(
                            cartInfo.getItemList().stream()
                                    .map(ItemResponse::of)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 응답")
    public static class ItemResponse {
        @Schema(description = "상품 식별자", example = "1")
        private Long itemId;

        @Schema(description = "상품 이름", example = "코트")
        private String itemName;

        @Schema(description = "상품 가격", example = "1000")
        private Long itemPrice;

        @Schema(description = "상품 응답")
        private ItemOptionResponse itemOption;

        public static ItemResponse of(
                CartInfo.ItemResponse itemResponse
        ) {
            return ItemResponse.builder()
                    .itemId(itemResponse.getItemId())
                    .itemName(itemResponse.getItemName())
                    .itemPrice(itemResponse.getItemPrice())
                    .itemOption(ItemOptionResponse.of(itemResponse.getItemOption()))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 옵션 응답")
    public static class ItemOptionResponse {
        @Schema(description = "상품 옵션 식별자", example = "1")
        private Long itemOptionId;

        @Schema(description = "상품 옵션 사이즈", example = "31")
        private String itemOptionSize;

        @Schema(description = "상품 옵션 색깔", example = "red")
        private String itemOptionColor;

        @Schema(description = "상품 옵션 가격", example = "500")
        private Long itemOptionPrice;

        @Schema(description = "수량", example = "10")
        private Long quantity;

        public static ItemOptionResponse of(
                CartInfo.ItemOptionResponse itemOptionResponse
        ) {
            return ItemOptionResponse.builder()
                    .itemOptionId(itemOptionResponse.getItemOptionId())
                    .itemOptionSize(itemOptionResponse.getItemOptionSize())
                    .itemOptionColor(itemOptionResponse.getItemOptionColor())
                    .itemOptionPrice(itemOptionResponse.getItemOptionPrice())
                    .quantity(itemOptionResponse.getQuantity())
                    .build();
        }
    }
}
