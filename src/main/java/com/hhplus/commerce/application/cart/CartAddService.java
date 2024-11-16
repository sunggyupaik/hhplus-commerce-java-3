package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartCommand;
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
    public Long addCart(CartCommand.AddRequest request) {
        customerReader.getCustomer(request.getCustomerId());

        Cart savedCart = cartReader.getCart(request.getCustomerId(), request.getItemOptionId());
        if (savedCart == null) {
            Cart cart = request.toEntity(request.getCustomerId());
            Cart createdCart = cartStore.save(cart);
            return createdCart.getId();
        }

        savedCart.addQuantity(request.getQuantity());
        return savedCart.getId();
    }
}
