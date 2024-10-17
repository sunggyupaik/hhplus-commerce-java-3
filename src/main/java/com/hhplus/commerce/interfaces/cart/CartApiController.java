package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.application.cart.CartQueryService;
import com.hhplus.commerce.application.cart.dto.CartItemResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.application.cart.dto.CartItemCommandResponse;
import com.hhplus.commerce.application.cart.dto.CartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartApiController implements CartSpecification {
    private final CartQueryService cartQueryService;

    @GetMapping
    public CommonResponse getCart(
            @RequestHeader(value = "customerId") Long customerId
    ) {
        List<CartItemResponse> cartItemResponseList = cartQueryService.getCart(customerId);
        return CommonResponse.success(cartItemResponseList);
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
