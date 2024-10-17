package com.hhplus.commerce.domain.Item;

import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;

public interface ItemStore {
    Item saveItem(Item item);

    ItemOption saveItemOption(ItemOption itemOption);
    ItemInventory saveItemInventory(ItemInventory itemInventory);
}
