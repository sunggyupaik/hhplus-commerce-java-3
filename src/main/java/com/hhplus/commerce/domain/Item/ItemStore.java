package com.hhplus.commerce.domain.Item;

import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventoryHistory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;

public interface ItemStore {
    Item saveItem(Item item);

    ItemOption saveItemOption(ItemOption itemOption);

    ItemInventory saveItemInventory(ItemInventory itemInventory);

    ItemInventoryHistory createItemInventoryHistory(ItemInventoryHistory itemInventoryHistory);
}
