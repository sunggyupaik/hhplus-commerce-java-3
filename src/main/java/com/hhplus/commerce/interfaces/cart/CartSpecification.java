package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.application.cart.dto.CartDeleteRequest;
import com.hhplus.commerce.application.cart.dto.CartItemRequest;
import com.hhplus.commerce.application.cart.dto.CartItemResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

@Tag(name = "cart", description = "장바구니 API")
public interface CartSpecification {
    @Operation(summary = "장바구니 조회", description = "💡주어진 고객 식별자에 해당하는 장바구니 상품을 반환합니다.")
    CommonResponse getCart(
            @Parameter(description = "고객 식별자") Long customerId
    );

    @Operation(summary = "장바구니 추가", description = "💡주어진 고객 식별자, 상품정보로 장바구니에 상품을 추가합니다.")
    CommonResponse addCart(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "상품 정보") CartItemRequest request
    );

    @Operation(summary = "장바구니 삭제", description = "💡주어진 고객, 상품, 상품옵션 식별자로 장바구니 상품을 삭제합니다.")
    CommonResponse deleteCart(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "상품 삭제 정보") CartDeleteRequest request
    );

    final class Fake implements CartSpecification {
        @Override
        public CommonResponse getCart(Long customerId) {
            CartItemResponse.CartItemOptionResponse cartItemOptionResponse1 = CartItemResponse.CartItemOptionResponse.builder()
                    .itemOptionId(1L)
                    .itemOptionSize("95")
                    .itemOptionColor("빨강")
                    .itemOptionPrice(0L)
                    .quantity(1L)
                    .build();

            CartItemResponse cartItemResponse1 = CartItemResponse.builder()
                    .itemId(21L)
                    .itemName("겨울 코트")
                    .itemPrice(10000L)
                    .cartItemOption(cartItemOptionResponse1)
                    .build();

            CartItemResponse.CartItemOptionResponse cartItemOptionResponse2 = CartItemResponse.CartItemOptionResponse.builder()
                    .itemOptionId(10L)
                    .itemOptionSize("105")
                    .itemOptionColor("파랑")
                    .itemOptionPrice(0L)
                    .quantity(12L)
                    .build();

            CartItemResponse cartItemResponse2 = CartItemResponse.builder()
                    .itemId(22L)
                    .itemName("가을 난방")
                    .itemPrice(20000L)
                    .cartItemOption(cartItemOptionResponse2)
                    .build();

            List<CartItemResponse> cartItemResponses = Arrays.asList(cartItemResponse1, cartItemResponse2);

            return CommonResponse.success(cartItemResponses);
        }

        @Override
        public CommonResponse addCart(Long customerId, CartItemRequest request) {
            return CommonResponse.success(1L);
        }

        @Override
        public CommonResponse deleteCart(Long customerId, CartDeleteRequest request) {
            return CommonResponse.success(1L);
        }
    }
}
