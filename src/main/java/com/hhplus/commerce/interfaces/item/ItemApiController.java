package com.hhplus.commerce.interfaces.item;

import com.hhplus.commerce.common.response.CommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemApiController implements ItemApiSpecification {
    @GetMapping("/{id}")
    public CommonResponse getItem(
            @PathVariable("id") Long id
    ) {
        return new ItemApiSpecification.Fake().getItem(id);
    }
    
    @GetMapping("/best")
    public CommonResponse getBestItems() {
        return new ItemApiSpecification.Fake().getBestItems();
    }
}
