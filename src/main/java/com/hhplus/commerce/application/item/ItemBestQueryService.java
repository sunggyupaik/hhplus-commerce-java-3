package com.hhplus.commerce.application.item;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.domain.Item.ItemInfo;
import com.hhplus.commerce.domain.order.OrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemBestQueryService {
    private final OrderReader orderReader;

    public ItemInfo.BestResult getBestItems() {
        List<ItemBestResponse> bestItems = orderReader.getBestItems();
        return ItemInfo.BestResult.of(bestItems);
    }

    @Cacheable(
            cacheNames = "ItemBestQueryService:getBestItems",
            key = "'getBestItems'",
            cacheManager = "thirtyMinutesCacheManager"
    )
    public ItemInfo.BestResult getBestItemsRedis() {
        List<ItemBestResponse> bestItems = orderReader.getBestItems();
        return ItemInfo.BestResult.of(bestItems);
    }
}
