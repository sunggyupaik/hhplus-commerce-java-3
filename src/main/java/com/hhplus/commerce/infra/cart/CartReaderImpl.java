package com.hhplus.commerce.infra.cart;

import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartReaderImpl implements CartReader {
    private final CartRepository cartRepository;

    @Override
    public List<Cart> getCarts(Long customerId) {
        return cartRepository.findByCustomerId(customerId);
    }
}
