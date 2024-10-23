package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderTest {
    @Autowired private OrderStore orderStore;

    @Test
    @DisplayName("주문이 주문시작 상태이면 결제가 가능하다")
    void testPaymentAvailable() {
        Order order = orderFixture();

        boolean paymentAvailable = order.paymentAvailable();

        Assertions.assertEquals(true, paymentAvailable,
                "주문이 주문시작 상태는 결제가 가능하다");
    }

    @Test
    @DisplayName("주문 시작 상태이면 주문 완료로 변경이 가능하다")
    void testChangeToOrderCompleteWithInitStatus() {
        Order order = orderFixture();

        order.changeToOrderComplete();
        boolean paymentAvailable = order.paymentAvailable();

        Assertions.assertEquals(OrderStatus.ORDER_COMPLETE, order.getStatus(),
                "주문 시작은 주문 완료로 변경된다");
        Assertions.assertEquals(false, paymentAvailable,
                "주문 완료 상태는 결제가 불가능하다");
    }

    @Test
    @DisplayName("주문의 총 가격은 옵션들의 가격을 수량대로 곱해서 모두 더한 값이다")
    void testCalculatePrice() {
        Order order = orderFixture();

        Long totalPrice = order.calculatePrice();


        Long expectedPrice = 0L;
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemOption orderItemOption = orderItem.getOrderItemOption();
            expectedPrice += (orderItem.getItemPrice() + orderItemOption.getItemOptionPrice()) * orderItem.getOrderCount();
        }

        Assertions.assertEquals(expectedPrice, totalPrice,
                "주문의 총 가격은 상품과 상품 옵션의 가격을 더한 후 상품의 갯수와 곱한 총 합이다");
    }

    //order
    private Order orderFixture() {
        Order order = createOrder();
        OrderItem orderItem_1 = createOrderItem_1(order, 5000L, 3);
        OrderItem orderItem_2 = createOrderItem_2(order, 3000L, 2);
        OrderItemOption orderItemOption_1 = createOrderItemOption(orderItem_1, 2000L);
        OrderItemOption orderItemOption_2 = createOrderItemOption(orderItem_2, 3000L);
        orderItem_1.changeOrderItemOption(orderItemOption_1);
        orderItem_2.changeOrderItemOption(orderItemOption_2);
        order.addOrderItem(orderItem_1);
        order.addOrderItem(orderItem_2);

        orderStore.save(order);
        orderStore.saveOrderItem(orderItem_1);
        orderStore.saveOrderItem(orderItem_2);
        orderStore.saveOrderItemOption(orderItemOption_1);
        orderStore.saveOrderItemOption(orderItemOption_2);

        return order;
    }

    private Order createOrder() {
        return Order.builder()
                .build();
    }

    private OrderItem createOrderItem_1(Order order, Long itemPrice, Integer orderCount) {
        return OrderItem.builder()
                .order(order)
                .itemPrice(itemPrice)
                .orderCount(orderCount)
                .build();
    }

    private OrderItem createOrderItem_2(Order order, Long itemPrice, Integer orderCount) {
        return OrderItem.builder()
                .order(order)
                .itemPrice(itemPrice)
                .orderCount(orderCount)
                .build();
    }

    private OrderItemOption createOrderItemOption(OrderItem orderItem, Long itemOptionPrice) {
        return OrderItemOption.builder()
                .orderItem(orderItem)
                .itemOptionPrice(itemOptionPrice)
                .build();
    }
}