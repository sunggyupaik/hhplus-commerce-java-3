package com.hhplus.commerce.application.item;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.domain.order.OrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemBestQueryService {
    private final OrderReader orderReader;

    public List<ItemBestResponse> getBestItems() {
        return orderReader.getBestItems();
    }
}
