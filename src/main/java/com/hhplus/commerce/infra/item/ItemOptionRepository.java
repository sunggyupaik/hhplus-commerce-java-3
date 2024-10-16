package com.hhplus.commerce.infra.item;

import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
}
