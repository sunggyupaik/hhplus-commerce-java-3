package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.domain.order.payment.Payment;

import java.util.List;

public interface OrderReader {
    Order getOrder(Long id);

    List<ItemBestResponse> getBestItems();

    Payment getPayment(Long orderId);
}
