package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.domain.Item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
