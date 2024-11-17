package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderCommand;
import com.hhplus.commerce.domain.order.OrderInfo;
import com.hhplus.commerce.domain.order.OrderStore;
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
    public OrderInfo.CreateResponse createOrder(OrderCommand.OrderRequest request) {
        Order savedOrder = orderStore.save(request.toEntity(request.getCustomerId()));

        //order aggregate
        request.getOrderItemList().forEach(orderItemRequest -> {
            OrderItem savedOrderItem = orderStore.saveOrderItem(orderItemRequest.toEntity(savedOrder));

            OrderCommand.OrderItemOptionRequest orderItemOptionRequest = orderItemRequest.getOrderItemOption();
            OrderItemOption orderItemOption = orderStore.saveOrderItemOption(orderItemOptionRequest.toEntity(savedOrderItem));

            savedOrderItem.changeOrderItemOption(orderItemOption);
            savedOrder.addOrderItem(savedOrderItem);
        });

        return OrderInfo.CreateResponse.of(savedOrder);
    }
}
