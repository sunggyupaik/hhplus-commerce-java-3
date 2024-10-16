package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemReaderImpl implements ItemReader {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;

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
}
