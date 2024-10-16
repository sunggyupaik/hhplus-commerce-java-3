package com.hhplus.commerce.application.cart;

import com.hhplus.commerce.application.cart.dto.CartItemResponse;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.domain.cart.Cart;
import com.hhplus.commerce.domain.cart.CartReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartQueryService {
    private final CartReader cartReader;
    private final ItemReader itemReader;

    public List<CartItemResponse> getCarts(Long customerId) {
        List<Cart> carts = cartReader.getCarts(customerId);

        return carts.stream()
                .map(cart -> {
                    Item item = itemReader.getItem(cart.getItemId());
                    ItemOption itemOption = itemReader.getItemOption(cart.getItemOptionId());

                    ItemInventory itemInventory = itemOption.getItemInventory();

                    CartItemResponse.CartItemOptionResponse cartItemOptionResponse =
                            CartItemResponse.CartItemOptionResponse.of(itemOption, itemInventory);

                    return CartItemResponse.of(item, cartItemOptionResponse);

                }).collect(Collectors.toList());
    }
}
