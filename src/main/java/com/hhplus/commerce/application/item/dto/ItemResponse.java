package com.hhplus.commerce.application.item.dto;

import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 응답")
public class ItemResponse {
    @Schema(description = "상품 식별자", example = "1")
    private Long itemId;

    @Schema(description = "상품 이름", example = "겨울 코트")
    private String itemName;

    @Schema(description = "상품 가격", example = "10000")
    private Long itemPrice;

    @Schema(description = "상품 옵션 목록")
    private List<ItemOptionResponse> itemOptionResponseList;

    public static ItemResponse of(Item item, List<ItemOptionResponse> itemOptionResponseList) {
        return ItemResponse.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemOptionResponseList(itemOptionResponseList)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 옵션 응답")
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

        public static ItemOptionResponse of(ItemOption itemOption, ItemInventory itemInventory) {
            return ItemOptionResponse.builder()
                    .itemOptionId(itemOption.getId())
                    .itemOptionSize(itemOption.getItemOptionSize())
                    .itemOptionColor(itemOption.getItemOptionColor())
                    .itemOptionPrice(itemOption.getItemOptionPrice())
                    .quantity(itemInventory.getQuantity())
                    .build();
        }
    }
}
