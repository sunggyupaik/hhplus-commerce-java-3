package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.cart.CartItemRequest;
import com.hhplus.commerce.domain.cart.CartItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/carts")
public class CartApiController implements CartSpecification {
    @GetMapping("")
    public CommonResponse getCart(
            @RequestHeader("Authorization") Long customerId
    ) {
        List<CartItemResponse> cartItemResponses = Arrays.asList(
                new CartItemResponse(1L, 2L, 3L),
                new CartItemResponse(1L, 3L, 5L),
                new CartItemResponse(1L, 4L, 10L)
        );

        return CommonResponse.success(cartItemResponses);
    }

    @PostMapping("")
    public CommonResponse addCart(
            @RequestHeader("Authorization") Long customerId,
            @RequestBody CartItemRequest request
    ) {
        CartItemResponse cartItemResponse = CartItemResponse.builder()
                .customerId(customerId)
                .itemId(request.getItemId())
                .quantity(request.getQuantity())
                .build();

        return CommonResponse.success(cartItemResponse);
    }

    @DeleteMapping("/item/{itemId}")
    public CommonResponse deleteCart(
            @RequestHeader("Authorization") Long customerId,
            @PathVariable("itemId") Long itemId
    ) {
        CartItemResponse cartItemResponse = CartItemResponse.builder()
                .customerId(customerId)
                .itemId(itemId)
                .quantity(3L)
                .build();

        return CommonResponse.success(cartItemResponse);
    }
}
