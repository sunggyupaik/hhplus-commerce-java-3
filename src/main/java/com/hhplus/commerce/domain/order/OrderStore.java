package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.OrderPayment;
import com.hhplus.commerce.domain.order.payment.OrderPaymentHistory;

public interface OrderStore {
    Order save(Order order);

    OrderItem saveOrderItem(OrderItem orderItem);

    OrderItemOption saveOrderItemOption(OrderItemOption orderItemOption);

    OrderPayment savePayment(OrderPayment orderPayment);

    OrderPaymentHistory saveOrderPaymentHistory(OrderPaymentHistory orderPaymentHistory);
}
