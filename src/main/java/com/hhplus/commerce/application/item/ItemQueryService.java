package com.hhplus.commerce.application.item;

import com.hhplus.commerce.application.item.dto.ItemResponse;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemQueryService {
    private final ItemReader itemReader;

    @Transactional(readOnly = true)
    public ItemResponse getItem(Long id) {
        Item item = itemReader.getItem(id);

        //item aggregate
        List<ItemResponse.ItemOptionResponse> itemOptionResponses = item.getItemOptions().stream()
                .map(itemOption -> ItemResponse.ItemOptionResponse.of(itemOption, itemOption.getItemInventory()))
                .toList();

        return ItemResponse.of(item, itemOptionResponses);
    }
}
