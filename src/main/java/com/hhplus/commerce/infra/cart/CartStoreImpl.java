package com.hhplus.commerce.infra.cart;

import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartStoreImpl implements CartStore {
    private final CartRepository cartRepository;

    @Override
    public void deleteCart(Long customerId, Long itemOptionId) {
        cartRepository.deleteCartByCustomerIdAndItemOptionId(customerId, itemOptionId);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
}
