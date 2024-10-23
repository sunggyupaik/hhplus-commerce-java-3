package com.hhplus.commerce.application.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상위 상품 응답")
public interface ItemBestResponse {
    @Schema(description = "상품 식별자", example = "1")
    Long getItemId();

    @Schema(description = "상푼 판매 갯수", example = "10")
    Long getCount();
}
