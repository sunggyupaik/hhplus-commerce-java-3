package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dto.OrderResultResponse;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
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
    public Order getOrder(Long id) {
        return orderReader.getOrder(id);
    }

    @Transactional
    public Order getOrderWithPessimisticLock(Long id) {
        return orderReader.getOrderWithPessimisticLock(id);
    }

    @Transactional
    public List<Order> getInitOrders() {
        return orderReader.getInitOrders();
    }

    @Transactional
    public List<OrderResultResponse.OrderDetailResponse> getDetailOrders(Long customerId) {
        List<Order> orders = orderReader.getOrderByCustomerId(customerId);

        return orders.stream()
                .map(order -> {
                    var orderResponse = OrderResultResponse.OrderResponse.of(order);

                    List<OrderItem> orderItems = order.getOrderItems();
                    List<OrderResultResponse.OrderItemSeries> orderItemSeries = orderItems.stream()
                            .map(orderItem -> {
                                OrderItemOption orderItemOption = orderItem.getOrderItemOption();

                                var orderItemResponse = OrderResultResponse.OrderItemResponse.of(orderItem);
                                var orderItemOptionResponse = OrderResultResponse.OrderItemOptionResponse.of(orderItemOption);
                                return OrderResultResponse.OrderItemSeries.of(orderItemResponse, orderItemOptionResponse);
                            }).collect(Collectors.toList());
                    return OrderResultResponse.OrderDetailResponse.of(orderResponse, orderItemSeries);
                }).collect(Collectors.toList());
    }
}
