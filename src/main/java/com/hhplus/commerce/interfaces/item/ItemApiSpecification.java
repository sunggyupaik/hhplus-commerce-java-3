package com.hhplus.commerce.interfaces.item;

import com.hhplus.commerce.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;

@Tag(name = "item", description = "상품 API")
public interface ItemApiSpecification {
    @Operation(summary = "상품 조회", description = "💡주어진 식별자에 해당하는 상품을 반환합니다.")
    CommonResponse getItem(
            @Parameter(description = "상품 식별자")  Long id
    );

    @Operation(summary = "상위 상품 조회", description = "💡3일간 가장 많이 팔린 상품 5개를 반환합니다.")
    CommonResponse getBestItems();

    final class Fake implements ItemApiSpecification {
        @Override
        public CommonResponse getItem(Long id) {
            ItemDto.ItemOptionResponse itemOptionResponse = ItemDto.ItemOptionResponse.builder()
                    .itemOptionId(10L)
                    .itemOptionSize("95")
                    .itemOptionColor("빨강")
                    .itemOptionPrice(0L)
                    .quantity(10L)
                    .build();

            ItemDto.DetailResponse itemResponse = ItemDto.DetailResponse.builder()
                    .itemId(id)
                    .itemName("겨울 코트")
                    .itemPrice(10000L)
                    .itemOptionList(List.of(itemOptionResponse))
                    .build();

            return CommonResponse.success(itemResponse);
        }

        @Override
        public CommonResponse getBestItems() {
            ItemDto.ItemOptionResponse itemOptionResponse1 = new ItemDto.ItemOptionResponse(
                    1L, "95", "파랑", 0L, 15L
            );
            ItemDto.DetailResponse itemResponse1 = new ItemDto.DetailResponse(
                    11L, "가을 코트", 40000L, List.of(itemOptionResponse1)
            );

            ItemDto.ItemOptionResponse itemOptionResponse2 = new ItemDto.ItemOptionResponse(
                    2L, "100", "빨강", 0L, 25L
            );
            ItemDto.DetailResponse itemResponse2 = new ItemDto.DetailResponse(
                    22L, "가을 코트", 30000L, List.of(itemOptionResponse2)
            );

            ItemDto.ItemOptionResponse itemOptionResponse3 = new ItemDto.ItemOptionResponse(
                    3L, "105", "노랑", 0L, 20L
            );
            ItemDto.DetailResponse itemResponse3 = new ItemDto.DetailResponse(
                    33L, "여성 코트", 25000L, List.of(itemOptionResponse3)
            );

            ItemDto.ItemOptionResponse itemOptionResponse4 = new ItemDto.ItemOptionResponse(
                    4L, "95", "파랑", 0L, 5L
            );
            ItemDto.DetailResponse itemResponse4 = new ItemDto.DetailResponse(
                    44L, "남성 코트", 10000L, List.of(itemOptionResponse4)
            );

            ItemDto.ItemOptionResponse itemOptionResponse5 = new ItemDto.ItemOptionResponse(
                    5L, "100", "검정", 0L, 10L
            );
            ItemDto.DetailResponse itemResponse5 = new ItemDto.DetailResponse(
                    55L, "겨울 코트", 20000L, List.of(itemOptionResponse5)
            );

            List<ItemDto.DetailResponse> iemResponses = Arrays.asList(
                    itemResponse1, itemResponse2, itemResponse3, itemResponse4, itemResponse5
            );

            return CommonResponse.success(iemResponses);
        }
    }
}
