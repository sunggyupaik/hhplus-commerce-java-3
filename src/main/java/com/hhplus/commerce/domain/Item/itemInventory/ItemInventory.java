package com.hhplus.commerce.domain.Item.itemInventory;

import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_inventory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ItemInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
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


    public void changeItemOption(ItemOption itemOption) {
        this.itemOption = itemOption;
    }

    public Long decreaseStock(Long quantity) {
        this.quantity -= quantity;
        if (this.quantity < 0) {
            throw new IllegalStatusException(ErrorCode.ITEM_STOCK_INSUFFICIENT);
        }

        return this.quantity;
    }

}
