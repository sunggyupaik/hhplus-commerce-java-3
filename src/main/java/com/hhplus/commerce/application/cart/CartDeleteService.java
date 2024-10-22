package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartDeleteRequest;
import com.hhplus.commerce.domain.cart.CartReader;
import com.hhplus.commerce.domain.cart.CartStore;
import com.hhplus.commerce.domain.customer.CustomerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDeleteService {
    private final CartStore cartStore;
    private final CartReader cartReader;
    private final CustomerReader customerReader;

    @Transactional
    public Integer deleteCart(Long customerId, CartDeleteRequest request) {
        customerReader.getCustomer(customerId);

        List<Long> itemOptionIds = request.getItemOptionIds();
        for (Long itemOptionId : itemOptionIds) {
            if (cartReader.exists(customerId, itemOptionId)) {
                cartStore.deleteCart(customerId, itemOptionId);
            }
        }

        return request.getItemOptionIds().size();
    }
}
