package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.cart.dto.CartItemCommandResponse;
import com.hhplus.commerce.domain.cart.dto.CartItemRequest;
import com.hhplus.commerce.domain.cart.dto.CartItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/carts")
public class CartApiController implements CartSpecification {
    @GetMapping
    public CommonResponse getCart(
            @RequestHeader(value = "customerId") Long customerId
    ) {
        CartItemResponse.CartItemOptionResponse cartItemOptionResponse1 = CartItemResponse.CartItemOptionResponse.builder()
                .itemOptionId(1L)
                .itemOptionSize("95")
                .itemOptionColor("빨강")
                .itemOptionPrice(0L)
                .quantity(1L)
                .build();

        CartItemResponse cartItemResponse1 = CartItemResponse.builder()
                .customerId(customerId)
                .itemId(21L)
                .itemName("겨울 코트")
                .itemPrice(10000L)
                .cartItemOptionResponse(cartItemOptionResponse1)
                .build();

        CartItemResponse.CartItemOptionResponse cartItemOptionResponse2 = CartItemResponse.CartItemOptionResponse.builder()
                .itemOptionId(10L)
                .itemOptionSize("105")
                .itemOptionColor("파랑")
                .itemOptionPrice(0L)
                .quantity(12L)
                .build();

        CartItemResponse cartItemResponse2 = CartItemResponse.builder()
                .customerId(customerId)
                .itemId(22L)
                .itemName("가을 난방")
                .itemPrice(20000L)
                .cartItemOptionResponse(cartItemOptionResponse2)
                .build();

        List<CartItemResponse> cartItemResponses = Arrays.asList(cartItemResponse1, cartItemResponse2);

        return CommonResponse.success(cartItemResponses);
    }

    @PostMapping
    public CommonResponse addCart(
            @RequestHeader("customerId") Long customerId,
            @RequestBody CartItemRequest request
    ) {
        CartItemCommandResponse cartItemCommandResponse = CartItemCommandResponse.builder()
                .customerId(customerId)
                .itemId(request.getItemId())
                .quantity(request.getQuantity())
                .build();

        return CommonResponse.success(cartItemCommandResponse);
    }

    @DeleteMapping
    public CommonResponse deleteCart(
            @RequestHeader("customerId") Long customerId,
            @RequestBody CartItemRequest request
    ) {
        CartItemCommandResponse cartItemCommandResponse = CartItemCommandResponse.builder()
                .customerId(customerId)
                .itemId(request.getItemId())
                .itemOptionId(request.getItemOptionId())
                .quantity(10L)
                .build();

        return CommonResponse.success(cartItemCommandResponse);
    }
}
