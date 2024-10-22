package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.application.cart.CartAddService;
import com.hhplus.commerce.application.cart.CartDeleteService;
import com.hhplus.commerce.application.cart.CartQueryService;
import com.hhplus.commerce.application.cart.dto.CartDeleteRequest;
import com.hhplus.commerce.application.cart.dto.CartItemRequest;
import com.hhplus.commerce.application.cart.dto.CartItemResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/carts")
public class CartApiController implements CartSpecification {
    private final CartQueryService cartQueryService;
    private final CartAddService cartAddService;
    private final CartDeleteService cartDeleteService;

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
        Long cartId = cartAddService.addCart(customerId, request);

        return CommonResponse.success(cartId);
    }

    @DeleteMapping
    public CommonResponse deleteCart(
            @RequestHeader("customerId") Long customerId,
            @RequestBody CartDeleteRequest request
    ) {
        Integer count = cartDeleteService.deleteCart(customerId, request);

        return CommonResponse.success(count);
    }
}
