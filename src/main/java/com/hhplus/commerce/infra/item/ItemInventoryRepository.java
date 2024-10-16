package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
    Optional<ItemInventory> findByItemOptionId(Long itemOptionId);
}
