package com.hhplus.commerce.interfaces.item;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.domain.Item.ItemInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class ItemDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 응답")
    public static class DetailResponse {
        @Schema(description = "상품 식별자", example = "1")
        private Long itemId;

        @Schema(description = "상품 이름", example = "겨울 코트")
        private String itemName;

        @Schema(description = "상품 가격", example = "10000")
        private Long itemPrice;

        @Schema(description = "상품 옵션 목록")
        private List<ItemOptionResponse> itemOptionList;

        public static DetailResponse of(ItemInfo.DetailResponse itemInfo) {
            return DetailResponse.builder()
                    .itemId(itemInfo.getItemId())
                    .itemName(itemInfo.getItemName())
                    .itemPrice(itemInfo.getItemPrice())
                    .itemOptionList(
                            itemInfo.getItemOptionList().stream()
                            .map(ItemOptionResponse::of).collect(Collectors.toList())
                    )
                    .build();
        }
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

        public static ItemOptionResponse of(ItemInfo.ItemOptionResponse itemOption) {
            return ItemOptionResponse.builder()
                    .itemOptionId(itemOption.getItemOptionId())
                    .itemOptionSize(itemOption.getItemOptionSize())
                    .itemOptionColor(itemOption.getItemOptionColor())
                    .itemOptionPrice(itemOption.getItemOptionPrice())
                    .quantity(itemOption.getQuantity())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "최다 판매 상품 목록 응답")
    public static class BestResult {
        @Schema(description = "최대 판매 상품 목록")
        private List<BestResponse> result;

        public static BestResult of(ItemInfo.BestResult itemInfo) {
            return BestResult.builder()
                    .result(
                            itemInfo.getResult().stream()
                            .map(BestResponse::of)
                            .collect(Collectors.toList())
                    )
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "최다 판매 상품 응답")
    public static class BestResponse {
        @Schema(description = "상품 식별자")
        private Long getItemId;

        @Schema(description = "판매 상품 갯수")
        private Long getCount;

        public static BestResponse of(ItemBestResponse response) {
            return BestResponse.builder()
                    .getItemId(response.getItemId())
                    .getCount(response.getCount())
                    .build();
        }
    }
}
