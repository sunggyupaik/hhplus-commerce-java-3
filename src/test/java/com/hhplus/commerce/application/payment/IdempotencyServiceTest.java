package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerStore;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.payment.*;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointStore;
import com.hhplus.commerce.infra.customer.CustomerRepository;
import com.hhplus.commerce.infra.item.ItemInventoryRepository;
import com.hhplus.commerce.infra.item.ItemOptionRepository;
import com.hhplus.commerce.infra.item.ItemRepository;
import com.hhplus.commerce.infra.order.OrderItemOptionRepository;
import com.hhplus.commerce.infra.order.OrderItemRepository;
import com.hhplus.commerce.infra.order.OrderRepository;
import com.hhplus.commerce.infra.payment.PaymentHistoryRepository;
import com.hhplus.commerce.infra.payment.PaymentIdempotencyRepository;
import com.hhplus.commerce.infra.payment.PaymentRepository;
import com.hhplus.commerce.infra.point.PointRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class IdempotencyServiceTest {
    @Autowired private IdempotencyService idempotencyService;
    @Autowired private PaymentReader paymentReader;
    @Autowired private CustomerStore customerStore;
    @Autowired private PointStore pointStore;
    @Autowired private OrderStore orderStore;
    @Autowired private PaymentStore paymentStore;

    //DB 초기화용
    @Autowired private PointRepository pointRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PaymentHistoryRepository paymentHistoryRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemOptionRepository itemOptionRepository;
    @Autowired private ItemInventoryRepository itemInventoryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private OrderItemOptionRepository orderItemOptionRepository;
    @Autowired private PaymentIdempotencyRepository paymentIdempotencyRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
        pointRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();
        paymentHistoryRepository.deleteAllInBatch();
        paymentIdempotencyRepository.deleteAllInBatch();

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
    @DisplayName("멱등성 키가 없으면 결제 요청을 실패한다")
    void idempotencyCheckWithIdempotencyNull() throws InterruptedException {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        Payment payment = paymentFixture(order.getId(), customer.getId(), "TOSS", 15000L);
        PaymentRequest paymentRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 15000L, null
        );

        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    idempotencyService.idempotencyCheck(paymentRequest);
                    success.incrementAndGet();
                } catch (InvalidParamException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 0,
                "멱등성 키가 null이면 성공은 0건이다");
        Assertions.assertEquals(fail.get(), 10,
                "멱등성 키가 null이면 10건의 시도는 모두 실패한다");
    }

    @Test
    @DisplayName("멱등성 키는 같지만 요청 내용이 다르면 결제 요청을 실패한다")
    void idempotencyCheckWithSameKeyNotSamePayload() throws InterruptedException {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        Payment payment = paymentFixture(order.getId(), customer.getId(), "TOSS", 15000L);
        PaymentIdempotency paymentIdempotency = paymentIdempotencyFixture(order.getId(), "123");
        PaymentRequest paymentRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 20000L, "123"
        );

        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    idempotencyService.idempotencyCheck(paymentRequest);
                    success.incrementAndGet();
                } catch (InvalidParamException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 0,
                "멱등성 키가 같아도 요청 body가 다르면 성공은 0건이다");
        Assertions.assertEquals(fail.get(), 10,
                "멱등성 키가 같아도 요청 body가 다르면 10건의 시도는 모두 실패한다");
    }

    //payment
    private Payment paymentFixture(Long orderId, Long customerId, String paymentMethod, Long amount) {
        Payment payment = createPayment(orderId, customerId, paymentMethod, amount);

        return paymentStore.savePayment(payment);
    }

    private Payment createPayment(Long orderId, Long customerId, String paymentMethod, Long amount) {
        return Payment.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .amount(amount)
                .build();
    }

    //idempotency
    private PaymentIdempotency paymentIdempotencyFixture(Long orderId, String idempotencyKey) {
        PaymentIdempotency paymentIdempotency = createPaymentIdempotency(orderId, idempotencyKey);

        return paymentStore.savePaymentIdempotency(paymentIdempotency);
    }

    private PaymentIdempotency createPaymentIdempotency(Long orderId, String idempotencyKey) {
        return PaymentIdempotency.builder()
                .orderId(orderId)
                .idempotencyKey(idempotencyKey)
                .build();
    }

    //point
    private Point pointFixture(Long customerId, Long pointAmount) {
        Point point = createPoint(customerId, pointAmount);

        return pointStore.save(point);
    }

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
    private PaymentRequest createPaymentRequest(
            Long orderId, Long customerId, String paymentMethod, Long amount, String idempotencyKey) {
        return PaymentRequest.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .idempotencyKey(idempotencyKey)
                .build();
    }
}
