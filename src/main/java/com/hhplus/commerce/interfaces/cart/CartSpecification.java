package com.hhplus.commerce.interfaces.cart;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.cart.dto.CartItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

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
            @Parameter(description = "상품 삭제 정보") CartItemRequest request
    );
}
