package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.domain.cart.CartCommand;
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
    public Integer deleteCart(CartCommand.DeleteRequest request) {
        customerReader.getCustomer(request.getCustomerId());

        List<Long> itemOptionIds = request.getItemOptionIdList();
        for (Long itemOptionId : itemOptionIds) {
            cartStore.deleteCart(request.getCustomerId(), itemOptionId);
        }

        return request.getItemOptionIdList().size();
    }
}
