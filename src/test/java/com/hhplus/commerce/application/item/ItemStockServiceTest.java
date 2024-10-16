package com.hhplus.commerce.application.item;

import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("ItemStockService 클래스")
class ItemStockServiceTest {
    private ItemReader itemReader;
    private ItemStockService itemStockService;

    @BeforeEach
    void setUp() {
        itemReader = mock(ItemReader.class);
        itemStockService = new ItemStockService(itemReader);
    }

    @Nested
    @DisplayName("decreaseStock 메소드는")
    class Describe_decreaseStock {
        @Nested
        @DisplayName("만약 존재하는 상품 옵션 식별자와 차감 갯수가 주어진다면")
        class Context_with_existed_item_option_id_and_quantity {
            private final Long existedItemInventoryId = 1L;
            private final Long existedItemOptionId = 2L;
            private final Long quantity = 10L;

            @Test
            @DisplayName("해당 갯수만큼 차감하고 남은 재고 수를 반환한다")
            void it_decreases_stock_and_returns_left_quantity() {
                ItemInventory itemInventory = createItemInventory(existedItemInventoryId, 20L);
                given(itemReader.getItemInventory(existedItemOptionId)).willReturn(itemInventory);

                Long leftQuantity = itemStockService.decreaseStock(existedItemOptionId, quantity);

                assertThat(leftQuantity).isEqualTo(20L - quantity);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 상품 옵션 식별자가 주어진다면")
        class Context_with_not_existed_item_option_id {
            private final Long notExistedItemOptionId = 99L;

            @Test
            @DisplayName("상품 재고가 존재하지 않다는 예외를 반환한다")
            void it_throws_item_inventory_not_exists() {
                given(itemReader.getItemInventory(notExistedItemOptionId)).willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> itemStockService.decreaseStock(notExistedItemOptionId, 10L)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 상품 옵션 식별자와 재고를 초과하는 차감 갯수가 주어진다면")
        class Context_with_existed_item_option_id_and_over_quantity {
            private final Long existedItemInventoryId = 1L;
            private final Long existedItemOptionId = 2L;
            private final Long quantity = 100L;

            @Test
            @DisplayName("상품 재고가 부족하다는 예외를 반환한다")
            void it_throws_item_stock_insufficient() {
                ItemInventory itemInventory = createItemInventory(existedItemInventoryId, 20L);
                given(itemReader.getItemInventory(existedItemOptionId)).willReturn(itemInventory);

                assertThatThrownBy(
                        () -> itemStockService.decreaseStock(existedItemOptionId, quantity)
                )
                        .isInstanceOf(IllegalStatusException.class);
            }
        }
    }

    private ItemInventory createItemInventory(Long id, Long quantity) {
        return ItemInventory.builder()
                .id(id)
                .quantity(quantity)
                .build();
    }
}