package com.hhplus.commerce.domain.Item.itemInventory;

import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_inventory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ItemOption itemOption;

    private Long quantity;

    @Builder
    public ItemInventory(
            Long id,
            ItemOption itemOption,
            Long quantity
    ) {
        this.id = id;
        this.itemOption = itemOption;
        this.quantity = quantity;
    }
}
