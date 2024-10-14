package com.hhplus.commerce.interfaces.item;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.Item.dto.ItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
public class ItemApiController implements ItemApiSpecification {
    @GetMapping("/{id}")
    public CommonResponse getItem(
            @PathVariable("id") Long id
    ) {
        ItemResponse.ItemOptionResponse itemOptionResponse = ItemResponse.ItemOptionResponse.builder()
                .itemOptionId(10L)
                .itemOptionSize("95")
                .itemOptionColor("빨강")
                .itemOptionPrice(0L)
                .quantity(10L)
                .build();

        ItemResponse itemResponse = ItemResponse.builder()
                .itemId(id)
                .itemName("겨울 코트")
                .itemPrice(10000L)
                .itemOptionResponse(itemOptionResponse)
                .build();

        return CommonResponse.success(itemResponse);
    }
    
    @GetMapping("/best")
    public CommonResponse getBestItems() {
        ItemResponse.ItemOptionResponse itemOptionResponse1 = new ItemResponse.ItemOptionResponse(
                1L, "95", "파랑", 0L, 15L
        );
        ItemResponse itemResponse1 = new ItemResponse(
                11L, "가을 코트", 40000L, itemOptionResponse1
        );

        ItemResponse.ItemOptionResponse itemOptionResponse2 = new ItemResponse.ItemOptionResponse(
                2L, "100", "빨강", 0L, 25L
        );
        ItemResponse itemResponse2 = new ItemResponse(
                22L, "가을 코트", 30000L, itemOptionResponse2
        );

        ItemResponse.ItemOptionResponse itemOptionResponse3 = new ItemResponse.ItemOptionResponse(
                3L, "105", "노랑", 0L, 20L
        );
        ItemResponse itemResponse3 = new ItemResponse(
                33L, "여성 코트", 25000L, itemOptionResponse3
        );

        ItemResponse.ItemOptionResponse itemOptionResponse4 = new ItemResponse.ItemOptionResponse(
                4L, "95", "파랑", 0L, 5L
        );
        ItemResponse itemResponse4 = new ItemResponse(
                44L, "남성 코트", 10000L, itemOptionResponse4
        );

        ItemResponse.ItemOptionResponse itemOptionResponse5 = new ItemResponse.ItemOptionResponse(
                5L, "100", "검정", 0L, 10L
        );
        ItemResponse itemResponse5 = new ItemResponse(
                55L, "겨울 코트", 20000L, itemOptionResponse5
        );

        List<ItemResponse> iemResponses = Arrays.asList(
                itemResponse1, itemResponse2, itemResponse3, itemResponse4, itemResponse5
        );

        return CommonResponse.success(iemResponses);
    }
}
