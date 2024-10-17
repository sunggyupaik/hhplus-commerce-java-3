package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.Payment;

public interface OrderStore {
    Order save(Order order);

    OrderItem saveOrderItem(OrderItem orderItem);

    OrderItemOption saveOrderItemOption(OrderItemOption orderItemOption);

    Payment savePayment(Payment payment);
}
