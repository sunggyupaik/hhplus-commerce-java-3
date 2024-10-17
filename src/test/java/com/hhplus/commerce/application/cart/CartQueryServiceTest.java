package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartItemResponse;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartReader;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("CartQueryService 클래스")
class CartQueryServiceTest {
    private CartQueryService cartQueryService;
    private ItemReader itemReader;
    private CartReader cartReader;
    private CustomerReader customerReader;

    @BeforeEach
    void setUp() {
        cartReader = mock(CartReader.class);
        itemReader = mock(ItemReader.class);
        customerReader = mock(CustomerReader.class);
        cartQueryService = new CartQueryService(cartReader, itemReader, customerReader);
    }

    @Nested
    @DisplayName("getCarts 메소드는")
    class Describe_getCarts {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자가 주어진다면")
        class Context_with_existed_customer_id {
            private final Long customerId = 1L;
            private final Long itemId = 2L;
            private final Long itemOptionId_1 = 3L;
            private final Long itemOptionId_2 = 4L;
            private final Long itemInventoryId_1 = 5L;
            private final Long itemInventoryId_2 = 6L;

            @Test
            @DisplayName("장바구니 상품 정보를 반환한다")
            void it_returns_item() {
                Customer customer = createCustomer(customerId);
                Item item = createItem(itemId, 100L, "코트");
                ItemOption itemOption_1 = createItemOption(itemOptionId_1, item, 100L, "blue", "95");
                ItemInventory itemInventory_1 = createItemInventory(itemInventoryId_1, itemOption_1, 10L);
                ItemOption itemOption_2 = createItemOption(itemOptionId_2, item, 200L, "yellow", "90");
                ItemInventory itemInventory_2 = createItemInventory(itemInventoryId_2, itemOption_2, 20L);
                Cart cart_1 = createCart(10L, customerId, itemId, itemOptionId_1, itemInventoryId_1);
                Cart cart_2 = createCart(11L, customerId, itemId, itemOptionId_2, itemInventoryId_2);
                List<Cart> carts = List.of(cart_1, cart_2);

                given(customerReader.getCustomer(customerId)).willReturn(customer);
                given(itemReader.getItem(itemId)).willReturn(item);
                given(itemReader.getItemOption(itemOptionId_1)).willReturn(itemOption_1);
                given(itemReader.getItemOption(itemOptionId_2)).willReturn(itemOption_2);
                given(itemReader.getItemInventory(itemOptionId_1)).willReturn(itemInventory_1);
                given(itemReader.getItemInventory(itemOptionId_2)).willReturn(itemInventory_2);
                given(cartReader.getCarts(customerId)).willReturn(carts);

                List<CartItemResponse> cartItemResponses = cartQueryService.getCart(customerId);

                assertThat(cartItemResponses).hasSize(2);

            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 고객 식별자가 주어진다면")
        class Context_with_not_existed_customer_id {
            private final Long notExistedCustomerId = 99L;

            @Test
            @DisplayName("고객을 찾을 수 없다는 예외를 반환한다")
            void it_throws_customer_not_exists() {
                given(customerReader.getCustomer(notExistedCustomerId)).willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> cartQueryService.getCart(notExistedCustomerId)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }
    }

    private Cart createCart(Long id, Long customerId, Long itemId, Long itemOptionId, Long quantity) {
        return Cart.builder()
                .id(id)
                .customerId(customerId)
                .itemId(itemId)
                .itemOptionId(itemOptionId)
                .quantity(quantity)
                .build();
    }

    private Customer createCustomer(Long id) {
        return Customer.builder()
                .id(id)
                .build();
    }

    private Item createItem(Long id, Long price, String name) {
        return Item.builder()
                .id(id)
                .itemPrice(price)
                .itemName(name)
                .build();
    }

    private ItemOption createItemOption(Long id, Item item, Long price, String color, String size) {
        return ItemOption.builder()
                .id(id)
                .item(item)
                .itemOptionPrice(price)
                .itemOptionColor(color)
                .itemOptionSize(size)
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