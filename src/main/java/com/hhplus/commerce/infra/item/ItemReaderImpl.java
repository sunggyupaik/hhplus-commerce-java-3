package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemReaderImpl implements ItemReader {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ItemInventoryRepository itemInventoryRepository;

    @Override
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ITEM_NOT_FOUND));
    }

    @Override
    public ItemOption getItemOption(Long itemOptionId) {
        return itemOptionRepository.findById(itemOptionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ITEM_OPTION_NOT_FOUND));
    }

    @Override
    public ItemInventory getItemInventory(Long itemOptionId) {
        return itemInventoryRepository.findByItemOptionId(itemOptionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ITEM_INVENTORY_NOT_FOUND));
    }

    @Override
    public ItemInventory getItemInventoryWithPessimisticLock(Long itemOptionId) {
        return itemInventoryRepository.findByItemOptionIdWithPessimisticLock(itemOptionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ITEM_INVENTORY_NOT_FOUND));
    }
}
