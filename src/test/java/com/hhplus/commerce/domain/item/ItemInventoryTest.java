package com.hhplus.commerce.domain.item;

import com.hhplus.commerce.support.exception.IllegalStatusException;
import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.infra.item.ItemInventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TearDownDatabase
public class ItemInventoryTest {
    @Autowired private ItemInventoryRepository itemInventoryRepository;

    @Test
    @DisplayName("재고수량 10개에서 5개를 차감하면 5개가 남는다")
    void decreaseStock() {
        ItemInventory itemInventory = ItemInventory.builder()
                .quantity(10L)
                .build();
        ItemInventory createdItemInventory = itemInventoryRepository.save(itemInventory);

        Long leftStock = createdItemInventory.decreaseStock(5L);

        Assertions.assertEquals(5L, leftStock,
                "재고수량 10개에서 5개를 차감하면 5개가 남는다");
    }

    @Test
    @DisplayName("재고수량 10개에서 20개를 차감하면 0보다 작다는 예외를 반환한다")
    void decreaseStockThrowsLessThanZero() {
        ItemInventory itemInventory = ItemInventory.builder()
                .quantity(10L)
                .build();
        ItemInventory createdItemInventory = itemInventoryRepository.save(itemInventory);

        assertThatThrownBy(
                () -> createdItemInventory.decreaseStock(20L)
        )
                .isInstanceOf(IllegalStatusException.class);
    }

    @Test
    @DisplayName("재고수량 10개에서 5개를 추가하면 15개가 남는다")
    void increaseStock() {
        ItemInventory itemInventory = ItemInventory.builder()
                .quantity(10L)
                .build();
        ItemInventory createdItemInventory = itemInventoryRepository.save(itemInventory);

        Long leftStock = createdItemInventory.increaseStock(5L);

        Assertions.assertEquals(15L, leftStock,
                "재고수량 10개에서 5개를 추가하면 15개가 남는다");
    }
}
