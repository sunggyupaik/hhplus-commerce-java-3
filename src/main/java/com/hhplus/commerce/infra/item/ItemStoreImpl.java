package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemStore;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemStoreImpl implements ItemStore {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ItemInventoryRepository itemInventoryRepository;

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public ItemOption saveItemOption(ItemOption itemOption) {
        return itemOptionRepository.save(itemOption);
    }

    @Override
    public ItemInventory saveItemInventory(ItemInventory itemInventory) {
        return itemInventoryRepository.save(itemInventory);
    }
}
