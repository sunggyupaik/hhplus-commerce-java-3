package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.application.cart.CartAddService;
import com.hhplus.commerce.application.cart.CartDeleteService;
import com.hhplus.commerce.application.cart.CartQueryService;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.cart.CartCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        var cartInfo = cartQueryService.getCart(customerId);
        CartDto.DetailResponse response = CartDto.DetailResponse.of(cartInfo);

        return CommonResponse.success(response);
    }

    @PostMapping
    public CommonResponse addCart(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid CartDto.AddRequest request
    ) {
        var cartCommand = CartCommand.AddRequest.of(customerId, request);
        Long cartId = cartAddService.addCart(cartCommand);

        return CommonResponse.success(cartId);
    }

    @DeleteMapping
    public CommonResponse deleteCart(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid CartDto.DeleteRequest request
    ) {
        var cartCommand = CartCommand.DeleteRequest.of(customerId, request);
        Integer count = cartDeleteService.deleteCart(cartCommand);

        return CommonResponse.success(count);
    }
}
