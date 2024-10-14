package com.hhplus.commerce.domain.Item.itemOption;

import com.hhplus.commerce.domain.Item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Item item;

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
}
