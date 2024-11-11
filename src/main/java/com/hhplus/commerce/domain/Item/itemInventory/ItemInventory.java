package com.hhplus.commerce.domain.Item.itemInventory;

import com.hhplus.commerce.common.BaseTimeEntity;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "item_inventories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ItemInventory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ToString.Exclude
    private ItemOption itemOption;

    @Version
    private Long Version;

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

    public Long increaseStock(Long quantity) {
        this.quantity += quantity;
        return this.quantity;
    }
}
