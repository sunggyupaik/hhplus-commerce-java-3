package com.hhplus.commerce.application.item;

import com.hhplus.commerce.application.item.dto.ItemResponse;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("ItemQueryService 클래스")
class ItemQueryServiceTest {
    private ItemQueryService itemQueryService;
    private ItemReader itemReader;

    @BeforeEach
    void setUp() {
        itemReader = mock(ItemReader.class);
        itemQueryService = new ItemQueryService(itemReader);
    }

    @Nested
    @DisplayName("getItem 메소드는")
    class Describe_getItem {
        @Nested
        @DisplayName("만약 존재하는 상품 식별자가 주어진다면")
        class Context_with_existed_item_id {
            private final Long existedItemId = 1L;
            private final Long existedItemOptionId = 2L;
            private final Long existedItemInventoryId = 3L;

            @Test
            @DisplayName("상품 정보를 반환한다")
            void it_returns_item() {
                Item item = createItem(existedItemId);
                ItemOption itemOption = createItemOption(existedItemOptionId, item);
                ItemInventory itemInventory = createItemInventory(existedItemInventoryId, itemOption, 10L);
                itemOption.changeInventory(itemInventory);
                item.addItemOption(itemOption);

                given(itemReader.getItem(existedItemId)).willReturn(item);

                ItemResponse itemResponse = itemQueryService.getItem(existedItemId);

                assertThat(itemResponse.getItemId()).isEqualTo(existedItemId);
                assertThat(itemResponse.getItemOptionResponseList()).hasSize(1)
                        .extracting("itemOptionId", "quantity")
                        .containsExactlyInAnyOrder(
                                Tuple.tuple(existedItemOptionId, 10L)
                        );
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 상품 식별자가 주어진다면")
        class Context_with_not_existed_item_id {
            private final Long notExistedItemId = 99L;

            @Test
            @DisplayName("상품이 존재하지 않다는 예외를 반환한다")
            void it_throws_item_not_exists() {
                given(itemReader.getItem(notExistedItemId)).willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> itemQueryService.getItem(notExistedItemId)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }
    }

    private Item createItem(Long id) {
        return Item.builder()
                .id(id)
                .build();
    }

    private ItemOption createItemOption(Long id, Item item) {
        return ItemOption.builder()
                .id(id)
                .item(item)
                .build();
    }

    private ItemInventory createItemInventory(Long id, ItemOption itemOption, Long quantity) {
        return ItemInventory.builder()
                .id(id)
                .itemOption(itemOption)
                .quantity(quantity)
                .build();
    }
}