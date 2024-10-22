package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartItemRequest;
import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartReader;
import com.hhplus.commerce.domain.cart.CartStore;
import com.hhplus.commerce.domain.customer.CustomerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartAddService {
    private final CartStore cartStore;
    private final CartReader cartReader;
    private final CustomerReader customerReader;

    @Transactional
    public Long addCart(Long customerId, CartItemRequest cartItemRequest) {
        customerReader.getCustomer(customerId);

        if (cartReader.exists(customerId, cartItemRequest.getItemOptionId())) {
            Cart cart = cartReader.getCart(customerId, cartItemRequest.getItemOptionId());
            cart.addQuantity(cartItemRequest.getQuantity());
            return cart.getId();
        } else {
            Cart cart = cartItemRequest.toEntity(customerId);
            Cart createdCart = cartStore.save(cart);
            return createdCart.getId();
        }
    }
}
