package com.hhplus.commerce.domain.Item.itemOption;

import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    private ItemInventory itemInventory;

    private String itemOptionSize;

    private String itemOptionColor;

    private Long itemOptionPrice;

    @Builder
    public ItemOption(
            Long id,
            Item item,
            String itemOptionSize,
            String itemOptionColor,
            Long itemOptionPrice
    ) {
        this.id = id;
        this.item = item;
        this.itemOptionSize = itemOptionSize;
        this.itemOptionColor = itemOptionColor;
        this.itemOptionPrice = itemOptionPrice;
    }

    public ItemOption changeInventory(ItemInventory itemInventory) {
        this.itemInventory = itemInventory;
        return this;
    }

    public static ItemOption of(ItemOption itemOption, ItemInventory itemInventory) {
        return ItemOption.builder()
                .id(itemOption.getId())
                .itemOptionSize(itemOption.getItemOptionSize())
                .itemOptionColor(itemOption.getItemOptionColor())
                .itemOptionPrice(itemOption.getItemOptionPrice())
                .build();
    }
}
