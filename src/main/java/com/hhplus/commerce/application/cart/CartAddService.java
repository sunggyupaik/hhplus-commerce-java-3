package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartItemRequest;
import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartReader;
import com.hhplus.commerce.domain.cart.CartStore;
import com.hhplus.commerce.domain.customer.CustomerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartAddService {
    private final CartStore cartStore;
    private final CartReader cartReader;
    private final CustomerReader customerReader;

    @Transactional
    public Cart addCart(Long customerId, List<CartItemRequest> requests) {
        customerReader.getCustomer(customerId);

        for (CartItemRequest cartItemRequest : requests) {
            if (cartReader.exists(customerId, cartItemRequest.getItemOptionId())) {
                Cart cart = cartReader.getCart(customerId, cartItemRequest.getItemOptionId());
                cart.addQuantity(cartItemRequest.getQuantity());
                return cartStore.save(cart);
            } else {
                Cart cart = cartItemRequest.toEntity(customerId);
                return cartStore.save(cart);
            }
        }

        return Cart.builder().build();
    }
}
