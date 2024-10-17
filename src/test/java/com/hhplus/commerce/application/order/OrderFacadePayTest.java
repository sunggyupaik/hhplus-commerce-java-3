package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerStore;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.Payment;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointStore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderFacadePayTest {
    private final OrderFacade orderFacade;
    private final OrderStore orderStore;
    private final OrderReader orderReader;
    private final PointStore pointStore;
    private final CustomerStore customerStore;

    private Order order;

    public OrderFacadePayTest(
            @Autowired OrderFacade orderFacade,
            @Autowired OrderStore orderStore,
            @Autowired OrderReader orderReader,
            @Autowired PointStore pointStore,
            @Autowired CustomerStore customerStore
    ) {
        this.orderFacade = orderFacade;
        this.orderStore = orderStore;
        this.orderReader = orderReader;
        this.pointStore = pointStore;
        this.customerStore = customerStore;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("주어진 결제정보에 따라 포인트를 차감하고 결제 정보를 생성한다.")
    void order() {
        Customer customer = customerFixture(null);
        Point point = pointFixture(customer.getId());
        order = orderFixture(customer.getId());
        PaymentRequest paymentRequest = createPaymentRequest(1L, 1L, "TOSS", 10000L);

        Long leftPoint = orderFacade.pay(order, paymentRequest);

        Assertions.assertEquals(order.calculatePrice(), 10000L, "5000원 2개 주문하므로 주문 가격은 총 10000원이다.");
        Assertions.assertEquals(customer.getId(), order.getCustomerId(), "주문자와 결제자는 똑같다.");
        Assertions.assertEquals(leftPoint, 10000L, "20000 포인트에서 10000원을 결제해 10000 포인트가 남는다.");

        Payment payment = orderReader.getPayment(order.getId());
        Assertions.assertEquals(payment.getId(), 1L, "결제가 정상이면 새로운 결제 정보가 생성된다.");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("결제 금액보다 포인트 잔액이 부족하면 예외를 반환한다")
    void orderWithOverAmount() {
        PaymentRequest paymentRequest = createPaymentRequest(1L, 1L, "TOSS", 20000L);

        assertThatThrownBy(
                () -> orderFacade.pay(order, paymentRequest)
        )
                .isInstanceOf(IllegalStatusException.class);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("결제금액과 요청금액이 다르면 예외를 반환한다.")
    void orderWithInvalidAmount() {
        Customer customer = customerFixture(null);
        Point point = pointFixture(customer.getId());
        order = orderFixture(customer.getId());

        PaymentRequest paymentRequest = createPaymentRequest(1L, 1L, "TOSS", 5000L);

        assertThatThrownBy(
                () -> orderFacade.pay(order, paymentRequest)
        )
                .isInstanceOf(InvalidParamException.class);
    }

    //point
    private Point pointFixture(Long customerId) {
        Point point = createPoint(customerId, 20000L);

        return pointStore.save(point);
    }

    //point
    private Point createPoint(Long customerId, Long point) {
        return Point.builder()
                .customerId(customerId)
                .point(point)
                .build();
    }

    //customer
    private Customer customerFixture(Long id) {
        Customer customer = createCustomer(id);

        return customerStore.save(customer);
    }

    private Customer createCustomer(Long id) {
        return Customer.builder()
                .id(id)
                .build();
    }

    //order
    private Order orderFixture(Long customerId) {
        Order order = createOrder(null, customerId);
        OrderItem orderItem = createOrderItem(null, 2, order);
        OrderItemOption orderItemOption = createOrderItemOption(null, orderItem);
        orderItem.changeOrderItemOption(orderItemOption);
        order.addOrderItem(orderItem);

        orderStore.save(order);
        orderStore.saveOrderItem(orderItem);
        orderStore.saveOrderItemOption(orderItemOption);

        return order;
    }

    private Order createOrder(Long id, Long customerId) {
        return Order.builder()
                .id(id)
                .customerId(customerId)
                .build();
    }

    private OrderItem createOrderItem(Long id, Integer orderCount, Order order) {
        return OrderItem.builder()
                .id(id)
                .orderCount(orderCount)
                .order(order)
                .itemPrice(5000L)
                .orderCount(2)
                .build();
    }

    private OrderItemOption createOrderItemOption(Long id, OrderItem orderItem) {
        return OrderItemOption.builder()
                .id(id)
                .oderItem(orderItem)
                .itemOptionPrice(0L)
                .build();
    }

    //payment
    private PaymentRequest createPaymentRequest(Long orderId, Long customerId, String paymentMethod, Long amount) {
        return PaymentRequest.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .build();
    }
}
