package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.Payment;
import com.hhplus.commerce.infra.order.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStoreImpl implements OrderStore {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderItemOptionRepository orderItemOptionRepository;
    private PaymentRepository paymentRepository;

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
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
