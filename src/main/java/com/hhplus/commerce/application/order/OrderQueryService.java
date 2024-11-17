package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderInfo;
import com.hhplus.commerce.domain.order.OrderReader;
import com.hhplus.commerce.domain.order.item.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderQueryService {
    private final OrderReader orderReader;

    @Transactional
    public Order getOrderWithPessimisticLock(Long id) {
        return orderReader.getOrderWithPessimisticLock(id);
    }

    @Transactional
    public List<Order> getInitOrders() {
        return orderReader.getInitOrders();
    }

    @Transactional
    public List<OrderInfo.DetailResponse> getDetailOrders(Long customerId) {
        List<Order> orders = orderReader.getOrderByCustomerId(customerId);

        return orders.stream()
                .map(order -> {
                    List<OrderItem> orderItems = order.getOrderItems();
                    List<OrderInfo.OrderItemResponse> orderItemResponses = orderItems.stream()
                            .map(orderItem -> OrderInfo.OrderItemResponse.of(
                                    orderItem,
                                    OrderInfo.OrderItemOptionResponse.of(orderItem.getOrderItemOption())
                            )).collect(Collectors.toList());

                    return OrderInfo.DetailResponse.of(order, orderItemResponses);
                }).collect(Collectors.toList());
    }
}
