package com.hhplus.commerce.domain.cart;

import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CartInfo {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private List<ItemResponse> itemList;

        public static DetailResponse of(List<ItemResponse> itemList) {
            return DetailResponse.builder()
                    .itemList(itemList)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemResponse {
        private Long itemId;
        private String itemName;
        private Long itemPrice;
        private ItemOptionResponse itemOption;

        public static ItemResponse of(Item item, ItemOptionResponse itemOptionResponse) {
            return ItemResponse.builder()
                    .itemId(item.getId())
                    .itemName(item.getItemName())
                    .itemPrice(item.getItemPrice())
                    .itemOption(itemOptionResponse)
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
}
