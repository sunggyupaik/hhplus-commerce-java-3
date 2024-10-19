package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerStore;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import com.hhplus.commerce.domain.order.OrderStatus;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.OrderPayment;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointStore;
import com.hhplus.commerce.infra.customer.CustomerRepository;
import com.hhplus.commerce.infra.item.ItemInventoryRepository;
import com.hhplus.commerce.infra.item.ItemOptionRepository;
import com.hhplus.commerce.infra.item.ItemRepository;
import com.hhplus.commerce.infra.order.OrderItemOptionRepository;
import com.hhplus.commerce.infra.order.OrderItemRepository;
import com.hhplus.commerce.infra.order.OrderRepository;
import com.hhplus.commerce.infra.order.payment.PaymentRepository;
import com.hhplus.commerce.infra.point.PointRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderFacadePayTest {
    @Autowired private OrderFacade orderFacade;
    @Autowired private OrderStore orderStore;
    @Autowired private OrderReader orderReader;
    @Autowired private PointStore pointStore;
    @Autowired private CustomerStore customerStore;

    //DB 초기화용
    @Autowired private PointRepository pointRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemOptionRepository itemOptionRepository;
    @Autowired private ItemInventoryRepository itemInventoryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private OrderItemOptionRepository orderItemOptionRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
        pointRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();

        itemAggregateDeleteAllInBatch();
        orderAggregateDeleteAllInBatch();
    }

    private void orderAggregateDeleteAllInBatch() {
        orderItemOptionRepository.deleteAllInBatch();
        orderItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    private void itemAggregateDeleteAllInBatch() {
        itemInventoryRepository.deleteAllInBatch();
        itemOptionRepository.deleteAllInBatch();
        itemRepository.deleteAllInBatch();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("주어진 결제 정보에 따라 포인트 차감, 결제 생성, 주문 완료로 변경, 데이터 플랫폼 전송한다")
    void order() {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        PaymentRequest paymentRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 10000L
        );

        Long leftPoint = orderFacade.pay(order, paymentRequest);

        Assertions.assertEquals(leftPoint, 10000L,
                "20000 포인트에서 10000원을 결제해 10000 포인트가 남는다");
        Assertions.assertEquals(order.calculatePrice(), 10000L,
                "5000원 2개 주문하므로 주문 가격은 총 10000원이다");
        Assertions.assertEquals(customer.getId(), order.getCustomerId(),
                "주문자와 결제자는 똑같다");
        OrderPayment orderPayment = orderReader.getPayment(order.getId());
        Assertions.assertEquals(orderPayment.getId(), 1L,
                "결제가 정상이면 새로운 결제 정보가 생성된다");
        Assertions.assertEquals(order.getStatus(), OrderStatus.ORDER_COMPLETE,
                "주문은 주문완료 상태로 변경된다.");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("결제 요청 금액보다 잔액 포인트가 부족하면 예외를 반환한다")
    void orderWithOverAmount() {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 10000L);
        Order order = orderFixture(customer.getId());
        PaymentRequest paymentRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 20000L
        );

        assertThatThrownBy(
                () -> orderFacade.pay(order, paymentRequest)
        )
                .isInstanceOf(IllegalStatusException.class);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("결제금액과 요청금액이 다르면 예외를 반환한다")
    void orderWithInvalidAmount() {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        PaymentRequest paymentRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 5000L
        );

        assertThatThrownBy(
                () -> orderFacade.pay(order, paymentRequest)
        )
                .isInstanceOf(InvalidParamException.class);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("주문이 주문시작 상태가 아니면 예외를 반환한다")
    void orderWithInvalidOrderStatus() {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        order.changeToOrderComplete();
        PaymentRequest paymentRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 5000L
        );

        assertThatThrownBy(
                () -> orderFacade.pay(order, paymentRequest)
        )
                .isInstanceOf(InvalidParamException.class);
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("같은 결제 요청을 동시에 10번하면 1번만 성공한다")
    void orderThrowsIllegalStatusException() throws InterruptedException {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        PaymentRequest paymentRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 10000L
        );

        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderFacade.pay(order, paymentRequest);
                    success.incrementAndGet();
                } catch (IllegalStatusException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 1,
                "같은 결제 요청은 10번 중 처음 1건만 성공한다");
        Assertions.assertEquals(fail.get(), 9,
                "같은 결제 요청은 10번 중 처음 이외 9건은 실패한다");
    }

    //point
    private Point pointFixture(Long customerId, Long pointAmount) {
        Point point = createPoint(customerId, pointAmount);

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
    private Customer customerFixture() {
        Customer customer = createCustomer();

        return customerStore.save(customer);
    }

    private Customer createCustomer() {
        return Customer.builder()
                .build();
    }

    //order
    private Order orderFixture(Long customerId) {
        Order order = createOrder(customerId);
        OrderItem orderItem = createOrderItem(2, order);
        OrderItemOption orderItemOption = createOrderItemOption(orderItem);
        orderItem.changeOrderItemOption(orderItemOption);
        order.addOrderItem(orderItem);

        orderStore.save(order);
        orderStore.saveOrderItem(orderItem);
        orderStore.saveOrderItemOption(orderItemOption);

        return order;
    }

    private Order createOrder(Long customerId) {
        return Order.builder()
                .customerId(customerId)
                .build();
    }

    private OrderItem createOrderItem(Integer orderCount, Order order) {
        return OrderItem.builder()
                .orderCount(orderCount)
                .order(order)
                .itemPrice(5000L)
                .orderCount(2)
                .build();
    }

    private OrderItemOption createOrderItemOption(OrderItem orderItem) {
        return OrderItemOption.builder()
                .orderItem(orderItem)
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
