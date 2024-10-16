package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.dto.OrderRequest;
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
    public Long createOrder(OrderRequest request) {
        Order savedOrder = orderStore.save(request.toEntity());

        //order aggregate
        request.getOrderItemList().forEach(orderItemRequest -> {
            OrderItem savedOrderItem = orderStore.saveOrderItem(orderItemRequest.toEntity(savedOrder));

            orderItemRequest.getOrderItemOptionList().forEach(orderItemOptionRequest -> {
                OrderItemOption savedOrderItemOption =
                        orderStore.saveOrderItemOption(orderItemOptionRequest.toEntity(savedOrderItem));
            });
        });

        return savedOrder.getId();
    }
}
