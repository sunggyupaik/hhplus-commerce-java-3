package com.hhplus.commerce.interfaces;

import com.hhplus.commerce.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "item", description = "상품 API")
public interface ItemApiSpecification {
    @Operation(summary = "상품 조회", description = "💡주어진 식별자에 해당하는 상품을 반환합니다.")
    CommonResponse getItem(@PathVariable Long id);

    @Operation(summary = "상위 상품 조회", description = "💡3일간 가장 많이 팔린 상품 5개를 반환합니다.")
    CommonResponse getBestItems();
}
