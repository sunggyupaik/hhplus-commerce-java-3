package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.OrderPayment;
import com.hhplus.commerce.domain.order.payment.OrderPaymentHistory;
import com.hhplus.commerce.infra.order.payment.PaymentHistoryRepository;
import com.hhplus.commerce.infra.order.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStoreImpl implements OrderStore {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItemOption saveOrderItemOption(OrderItemOption orderItemOption) {
        return orderItemOptionRepository.save(orderItemOption);
    }

    @Override
    public OrderPayment savePayment(OrderPayment orderPayment) {
        return paymentRepository.save(orderPayment);
    }

    @Override
    public OrderPaymentHistory saveOrderPaymentHistory(OrderPaymentHistory orderPaymentHistory) {
        return paymentHistoryRepository.save(orderPaymentHistory);
    }
}
