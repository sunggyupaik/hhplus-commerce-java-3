package com.hhplus.commerce.domain.cart;

import java.util.List;

public interface CartReader {
    List<Cart> getCarts(Long customerId);

    Cart getCart(Long customerId, Long itemOptionId);

    Cart findCart(Long customerId, Long itemOptionId);
}
