package com.hhplus.commerce.infra.cart;

import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.response.ErrorCode;
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

    @Override
    public Cart getCart(Long customerId, Long itemOptionId) {
        return cartRepository.findByCustomerIdAndItemOptionId(customerId, itemOptionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CART_NOT_FOUND));
    }

    @Override
    public boolean exists(Long customerId, Long itemOptionId) {
        return cartRepository.existsByCustomerIdAndItemOptionId(customerId, itemOptionId);
    }
}
