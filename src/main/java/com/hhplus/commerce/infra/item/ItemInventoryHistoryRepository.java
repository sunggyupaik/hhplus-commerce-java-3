package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.domain.Item.itemInventory.ItemInventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInventoryHistoryRepository extends JpaRepository<ItemInventoryHistory, Long> {
}
