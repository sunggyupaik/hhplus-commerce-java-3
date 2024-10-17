package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemInventoryRepository extends JpaRepository<ItemInventory, Long> {
    Optional<ItemInventory> findByItemOptionId(Long itemOptionId);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select iv from ItemInventory iv where iv.itemOption.id = :itemOptionId")
    Optional<ItemInventory> findByItemOptionIdWithPessimisticLock(@Param("itemOptionId") Long itemOptionId);
}
