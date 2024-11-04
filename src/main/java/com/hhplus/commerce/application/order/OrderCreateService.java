package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.application.order.dto.OrderRequest;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderCreateService {
    private final OrderStore orderStore;

    @Transactional
    public Order createOrder(Long customerId, OrderRequest request) {
        Order savedOrder = orderStore.save(request.toEntity(customerId));

        //order aggregate
        request.getOrderItemRequestList().forEach(orderItemRequest -> {
            OrderItem savedOrderItem = orderStore.saveOrderItem(orderItemRequest.toEntity(savedOrder));

            OrderRequest.OrderItemOptionRequest orderItemOptionRequest = orderItemRequest.getOrderItemOptionRequest();
            OrderItemOption orderItemOption = orderStore.saveOrderItemOption(orderItemOptionRequest.toEntity(savedOrderItem));

            savedOrderItem.changeOrderItemOption(orderItemOption);
            savedOrder.addOrderItem(savedOrderItem);
        });

        return savedOrder;
    }
}
