package com.hhplus.commerce.domain.Item;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ItemInfo {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long itemId;
        private String itemName;
        private Long itemPrice;
        private List<ItemOptionResponse> itemOptionList;

        public static DetailResponse of(Item item, List<ItemOptionResponse> itemOptionList) {
            return DetailResponse.builder()
                    .itemId(item.getId())
                    .itemName(item.getItemName())
                    .itemPrice(item.getItemPrice())
                    .itemOptionList(itemOptionList)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemOptionResponse {
        private Long itemOptionId;
        private String itemOptionSize;
        private String itemOptionColor;
        private Long itemOptionPrice;
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BestResult {
        private List<ItemBestResponse> result;

        public static BestResult of(List<ItemBestResponse> result) {
            return BestResult.builder()
                    .result(result)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BestResponse {
        private Long getItemId;
        private Long getCount;

        public static BestResponse of(ItemBestResponse response) {
            return BestResponse.builder()
                    .getItemId(response.getItemId())
                    .getCount(response.getCount())
                    .build();
        }
    }
}
