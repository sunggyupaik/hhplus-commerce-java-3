package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartDeleteRequest;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.domain.cart.CartReader;
import com.hhplus.commerce.domain.cart.CartStore;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("CartDeleteService 클래스")
class CartDeleteServiceTest {
    private CartDeleteService cartDeleteService;
    private CartStore cartStore;
    private CartReader cartReader;
    private CustomerReader customerReader;

    @BeforeEach
    void setUp() {
        cartStore = mock(CartStore.class);
        cartReader = mock(CartReader.class);
        customerReader = mock(CustomerReader.class);
        cartDeleteService = new CartDeleteService(cartStore, cartReader, customerReader);
    }

    @Nested
    @DisplayName("deleteCart 메소드는")
    class Describe_deleteCart {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자와 상품 옵션 목록이 주어진다면")
        class Context_with_existed_customer_id_and_itemOption_ids {
            private final Long customerId = 1L;
            private final List<Long> itemOptionIds = List.of(2L,3L,4L);
            private final Long itemId = 10L;
            private final Long itemOptionId = 2L;

            @Test
            @DisplayName("해당하는 장바구니 상품을 삭제한다")
            void it_deletes_carts() {
                CartDeleteRequest cartDeleteRequest = createCartDeleteRequest(itemOptionIds);
                given(cartReader.exists(customerId, 2L)).willReturn(true);
                given(cartReader.exists(customerId, 3L)).willReturn(false);
                given(cartReader.exists(customerId, 3L)).willReturn(false);

                cartDeleteService.deleteCart(customerId, cartDeleteRequest);

                verify(cartStore, times(1)).deleteCart(customerId, 2L);

            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 고객 식별자가 주어진다면")
        class Context_with_not_existed_customer_id {
            private final Long notExistedCustomerId = 99L;
            private final List<Long> itemOptionIds = List.of(2L,3L,4L);

            @Test
            @DisplayName("고객을 찾을 수 없다는 예외를 반환한다")
            void it_throws_customer_not_exists() {
                CartDeleteRequest cartDeleteRequest = createCartDeleteRequest(itemOptionIds);
                given(customerReader.getCustomer(notExistedCustomerId)).willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> cartDeleteService.deleteCart(notExistedCustomerId, cartDeleteRequest)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }
    }

    private CartDeleteRequest createCartDeleteRequest(List<Long> itemOptionIds) {
        return CartDeleteRequest.builder()
                .itemOptionIds(itemOptionIds)
                .build();
    }

    private Customer createCustomer(Long id) {
        return Customer.builder()
                .id(id)
                .build();
    }
}