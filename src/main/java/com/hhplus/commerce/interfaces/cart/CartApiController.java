package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.application.cart.CartQueryService;
import com.hhplus.commerce.application.cart.dto.CartItemRequest;
import com.hhplus.commerce.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartApiController implements CartSpecification {
    private final CartQueryService cartQueryService;

    @GetMapping
    public CommonResponse getCart(
            @RequestHeader(value = "customerId") Long customerId
    ) {
//        List<CartItemResponse> cartItemResponseList = cartQueryService.getCart(customerId);
//        return CommonResponse.success(cartItemResponseList);
        return new CartSpecification.Fake().getCart(customerId);
    }

    @PostMapping
    public CommonResponse addCart(
            @RequestHeader("customerId") Long customerId,
            @RequestBody CartItemRequest request
    ) {
        return new CartSpecification.Fake().addCart(customerId, request);
    }

    @DeleteMapping
    public CommonResponse deleteCart(
            @RequestHeader("customerId") Long customerId,
            @RequestBody CartItemRequest request
    ) {
        return new CartSpecification.Fake().deleteCart(customerId, request);
    }
}
