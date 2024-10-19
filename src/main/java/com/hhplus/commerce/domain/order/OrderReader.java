package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.domain.order.payment.OrderPayment;

import java.util.List;

public interface OrderReader {
    Order getOrder(Long id);

    List<ItemBestResponse> getBestItems();

    OrderPayment getPayment(Long orderId);
}
