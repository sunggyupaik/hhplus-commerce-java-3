package com.hhplus.commerce.application.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상위 상품 응답")
public class ItemBestResponse {
    @Schema(description = "상품 식별자", example = "1")
    private Long itemId;

    @Schema(description = "상품 판매 수", example = "100")
    private Long count;
}
