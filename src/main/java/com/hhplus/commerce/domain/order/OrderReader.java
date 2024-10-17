package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;

import java.util.List;

public interface OrderReader {
    Order getOrder(Long id);

    List<ItemBestResponse> getBestItems();
}
