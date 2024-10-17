package com.hhplus.commerce.domain.cart;

public interface CartStore {
    void deleteCart(Long customerId, Long itemOptionId);

    Cart save(Cart cart);
}
