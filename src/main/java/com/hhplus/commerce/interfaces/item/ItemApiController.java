package com.hhplus.commerce.interfaces.item;

import com.hhplus.commerce.application.item.ItemQueryService;
import com.hhplus.commerce.common.response.CommonResponse;
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

    @GetMapping("/{id}")
    public CommonResponse getItem(
            @PathVariable("id") Long id
    ) {
//        ItemResponse itemResponse = itemQueryService.getItem(id);
//        return CommonResponse.success(itemResponse);
        return new ItemApiSpecification.Fake().getItem(id);
    }
    
    @GetMapping("/best")
    public CommonResponse getBestItems() {
        //return new ItemApiSpecification.Fake().getBestItems();
        return new ItemApiSpecification.Fake().getBestItems();
    }
}
