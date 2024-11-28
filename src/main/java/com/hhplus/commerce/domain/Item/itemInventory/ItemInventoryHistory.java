package com.hhplus.commerce.domain.Item.itemInventory;

import com.hhplus.commerce.support.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "item_inventory_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ItemInventoryHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemOptionId;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private HistoryType historyType;

    public static ItemInventoryHistory createMinus(Long itemOptionId, Long quantity) {
        return ItemInventoryHistory.builder()
                .itemOptionId(itemOptionId)
                .quantity(quantity)
                .historyType(HistoryType.MINUS)
                .build();
    }

    public static ItemInventoryHistory createPlus(Long itemOptionId, Long quantity) {
        return ItemInventoryHistory.builder()
                .itemOptionId(itemOptionId)
                .quantity(quantity)
                .historyType(HistoryType.PLUS)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public enum HistoryType {
        PLUS("재고 사용량 증가"),
        MINUS("재고 사용량 감소");

        private final String description;
    }

    @Builder
    public ItemInventoryHistory(Long id, Long itemOptionId, Long quantity, HistoryType historyType) {
        this.id = id;
        this.itemOptionId = itemOptionId;
        this.quantity = quantity;
        this.historyType = historyType;
    }
}
