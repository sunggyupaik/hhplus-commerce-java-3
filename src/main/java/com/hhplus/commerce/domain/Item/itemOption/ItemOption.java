package com.hhplus.commerce.domain.Item.itemOption;

import com.hhplus.commerce.common.BaseTimeEntity;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "item_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ItemOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ToString.Exclude
    private Item item;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "itemOption", cascade = CascadeType.PERSIST)
    @ToString.Exclude
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

    public void changeItem(Item item) {
        this.item = item;
    }

    public ItemOption changeInventory(ItemInventory itemInventory) {
        this.itemInventory = itemInventory;
        itemInventory.changeItemOption(this);
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
