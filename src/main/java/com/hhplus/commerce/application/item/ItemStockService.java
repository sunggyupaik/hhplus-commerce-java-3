package com.hhplus.commerce.application.item;

import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemStockService {
    private final ItemReader itemReader;

    @Transactional
    public Long decreaseStock(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventoryWithPessimisticLock(itemOptionId);
        itemInventory.decreaseStock(quantity);

        return itemInventory.getQuantity();
    }
}
