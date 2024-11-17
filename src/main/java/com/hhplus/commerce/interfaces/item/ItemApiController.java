package com.hhplus.commerce.interfaces.item;

import com.hhplus.commerce.application.item.ItemBestQueryService;
import com.hhplus.commerce.application.item.ItemQueryService;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.Item.ItemInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemApiController implements ItemApiSpecification {
    private final ItemQueryService itemQueryService;
    private final ItemBestQueryService itemBestQueryService;

    @GetMapping("/{id}")
    public CommonResponse getItem(
            @PathVariable("id") Long id
    ) {
        var itemInfo = itemQueryService.getItem(id);
        ItemDto.DetailResponse response = ItemDto.DetailResponse.of(itemInfo);

        return CommonResponse.success(response);
    }
    
    @GetMapping("/best/r")
    public CommonResponse getBestItemsRedis() {
        var itemInfo = itemBestQueryService.getBestItemsRedis();
        ItemDto.BestResult bestResult = ItemDto.BestResult.of(itemInfo);

        return CommonResponse.success(bestResult);
    }

    @GetMapping("/best")
    public CommonResponse getBestItems() {
        ItemInfo.BestResult itemInfo = itemBestQueryService.getBestItems();
        ItemDto.BestResult bestResult = ItemDto.BestResult.of(itemInfo);

        return CommonResponse.success(bestResult);
    }
}
