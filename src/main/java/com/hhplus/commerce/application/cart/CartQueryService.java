package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartItemResponse;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartReader;
import com.hhplus.commerce.domain.customer.CustomerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartQueryService {
    private final CartReader cartReader;
    private final ItemReader itemReader;
    private final CustomerReader customerReader;

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart(Long customerId) {
        customerReader.getCustomer(customerId);

        List<Cart> carts = cartReader.getCarts(customerId);

        return carts.stream()
                .map(cart -> {
                    Item item = itemReader.getItem(cart.getItemId());
                    ItemOption itemOption = itemReader.getItemOption(cart.getItemOptionId());
                    ItemInventory itemInventory = itemReader.getItemInventory(itemOption.getId());

                    CartItemResponse.CartItemOptionResponse cartItemOptionResponse =
                            CartItemResponse.CartItemOptionResponse.of(itemOption, itemInventory);

                    return CartItemResponse.of(item, cartItemOptionResponse);

                }).collect(Collectors.toList());
    }
}
