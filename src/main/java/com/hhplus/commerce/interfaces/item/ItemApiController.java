package com.hhplus.commerce.interfaces.item;

import com.hhplus.commerce.application.item.ItemBestQueryService;
import com.hhplus.commerce.application.item.ItemQueryService;
import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.application.item.dto.ItemResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        ItemResponse itemResponse = itemQueryService.getItem(id);

        return CommonResponse.success(itemResponse);
    }
    
    @GetMapping("/best")
    public CommonResponse getBestItems() {
        List<ItemBestResponse> bestItems = itemBestQueryService.getBestItems();

        return CommonResponse.success(bestItems);
    }
}
