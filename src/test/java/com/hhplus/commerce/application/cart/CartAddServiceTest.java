package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartItemRequest;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartReader;
import com.hhplus.commerce.domain.cart.CartStore;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("CartAddService 클래스")
class CartAddServiceTest {
    private CartAddService cartAddService;
    private CartStore cartStore;
    private CartReader cartReader;
    private CustomerReader customerReader;

    @BeforeEach
    void setUp() {
        cartStore = mock(CartStore.class);
        cartReader = mock(CartReader.class);
        customerReader = mock(CustomerReader.class);
        cartAddService = new CartAddService(cartStore, cartReader, customerReader);
    }

    @Nested
    @DisplayName("addCart 메소드는")
    class Describe_addCart {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자와 싱품 요청 목록이 주어진다면")
        class Context_with_existed_customer_id_and_itemOption {
            private final Long customerId = 1L;

            @Test
            @DisplayName("장바구니에 상품을 추가한다")
            void it_add_carItem_to_cart() {
                Customer customer = createCustomer(customerId);
                CartItemRequest request = createCartItemRequest(10L, 2L, 3L);
                Cart cart = createCart(1L, 2L, 10L, 2L, 3L);
                given(customerReader.getCustomer(customerId)).willReturn(customer);
                given(cartReader.exists(customerId, 2L)).willReturn(false);
                given(cartStore.save(any(Cart.class))).willReturn(cart);

                cartAddService.addCart(customerId, request);

                verify(cartStore, times(1)).save(any(Cart.class));

            }
        }

        @Nested
        @DisplayName("만약 존재하는 고객 식별자와 기존에 존재하는 싱품 요청 목록이 주어진다면")
        class Context_with_existed_customer_id_and_itemOption_to_update {
            private final Long customerId = 1L;

            @Test
            @DisplayName("장바구니에 해당 상품의 수량을 더한다.")
            void it_add_carItem_to_cart() {
                Customer customer = createCustomer(customerId);
                CartItemRequest request = createCartItemRequest(10L, 2L, 3L);
                Cart existedCart = createCart(1L, 2L, 10L, 2L, 3L);
                Cart cart = createCart(2L, 2L, 10L, 2L, 3L);
                given(customerReader.getCustomer(customerId)).willReturn(customer);
                given(cartReader.exists(customerId, 2L)).willReturn(true);
                given(cartReader.getCart(customerId, 2L)).willReturn(existedCart);

                cartAddService.addCart(customerId, request);

                verify(cartStore, times(0)).save(any(Cart.class));

            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 고객 식별자가 주어진다면")
        class Context_with_not_existed_customer_id {
            private final Long notExistedCustomerId = 99L;

            @Test
            @DisplayName("고객을 찾을 수 없다는 예외를 반환한다")
            void it_throws_customer_not_exists() {
                CartItemRequest request = createCartItemRequest(1L, 2L, 3L);
                given(customerReader.getCustomer(notExistedCustomerId)).willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> cartAddService.addCart(notExistedCustomerId, request)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }
    }

    private Customer createCustomer(Long id) {
        return Customer.builder()
                .id(id)
                .build();
    }

    private CartItemRequest createCartItemRequest(Long itemId, Long itemOptionId, Long quantity) {
        return CartItemRequest.builder()
                .itemId(itemId)
                .itemOptionId(itemOptionId)
                .quantity(quantity)
                .build();
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
}