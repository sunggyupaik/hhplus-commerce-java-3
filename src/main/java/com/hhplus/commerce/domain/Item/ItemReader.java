package com.hhplus.commerce.domain.Item;

import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;

public interface ItemReader {
    Item getItem(Long id);

    ItemOption getItemOption(Long itemOptionId);

    ItemInventory getItemInventory(Long itemOptionId);
}
