package com.hhplus.commerce.application.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "장바구니 삭제 요청")
public class CartDeleteRequest {
    @Schema(description = "상품 옵션 식별자 목록", example = "[1, 2, 3]")
    List<Long> itemOptionIds;
}
