package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.order.OrderDataPlatformSendService;
import com.hhplus.commerce.application.order.dataPlatform.OrderDataPlatformEvent;
import com.hhplus.commerce.application.order.dataPlatform.OrderDataPlatformPayload;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStatus;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentCommand;
import com.hhplus.commerce.domain.payment.history.PaymentHistory;
import com.hhplus.commerce.domain.payment.idempotency.PaymentIdempotency;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.infra.customer.CustomerRepository;
import com.hhplus.commerce.infra.order.OrderRepository;
import com.hhplus.commerce.infra.payment.PaymentHistoryRepository;
import com.hhplus.commerce.infra.payment.PaymentIdempotencyRepository;
import com.hhplus.commerce.infra.payment.PaymentRepository;
import com.hhplus.commerce.infra.point.PointRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TearDownDatabase
public class PaymentFacadeTest {
    @Autowired private PaymentFacade paymentFacade;
    @Autowired private OrderStore orderStore;

    @Autowired private PaymentIdempotencyRepository paymentIdempotencyRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PointRepository pointRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PaymentHistoryRepository paymentHistoryRepository;
    @Autowired private OrderRepository orderRepository;
    @SpyBean private OrderDataPlatformSendService orderDataPlatformSendService;

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("주어진 결제 정보에 따라 포인트 차감, 결제 생성, 주문 완료로 변경, 데이터 플랫폼 전송한다")
    void pay() {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 10000L, "123"
        );

        paymentFacade.payOrder(payOrderRequest);

        Point findPoint = pointRepository.findById(point.getId()).orElseThrow();
        Assertions.assertEquals(findPoint.getPoint(), 10000L,
                "20000 포인트에서 10000원을 결제해 10000 포인트가 남는다");
        Assertions.assertEquals(order.calculatePrice(), 10000L,
                "5000원 2개 주문하므로 주문 가격은 총 10000원이다");
        Assertions.assertEquals(customer.getId(), order.getCustomerId(),
                "주문자와 결제자는 똑같다");

        List<Payment> all = paymentRepository.findAll();
        Assertions.assertEquals( all.size(), 1,
                "결제가 정상이면 새로운 결제 정보가 생성된다");

        Order findOrder = orderRepository.findById(order.getId()).orElseThrow();
        Assertions.assertEquals(findOrder.getStatus(), OrderStatus.ORDER_COMPLETE,
                "주문은 주문완료 상태로 변경된다.");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("결제 요청 금액보다 잔액 포인트가 부족하면 예외를 반환한다")
    void orderWithOverAmount() {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 10000L);
        Order order = orderFixture(customer.getId());
        PaymentIdempotency paymentIdempotency = paymentIdempotencyFixture(order.getId(), null);
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 20000L, "123"
        );

        assertThatThrownBy(
                () -> paymentFacade.payOrder(payOrderRequest)
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
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 5000L, "123"
        );

        assertThatThrownBy(
                () -> paymentFacade.payOrder(payOrderRequest)
        )
                .isInstanceOf(InvalidParamException.class);

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAll();
        assertThat(paymentHistories).hasSize(1);
        assertThat(paymentHistories.get(0).getCode()).isEqualTo(ErrorCode.PAYMENT_INVALID_PRICE.name());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("주문자와 결제자가 다르면 예외를 반환한다")
    void orderWithNotSameOrderCustomerAndPayCustomer() {
        Customer customer_1 = customerFixture();
        Customer customer_2 = customerFixture();
        Point point_1 = pointFixture(customer_1.getId(), 20000L);
        Point point_2 = pointFixture(customer_2.getId(), 20000L);
        Order order = orderFixture(customer_1.getId());
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer_2.getId(), "TOSS", 10000L, "123"
        );

        assertThatThrownBy(
                () -> paymentFacade.payOrder(payOrderRequest)
        )
                .isInstanceOf(InvalidParamException.class);

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAll();
        assertThat(paymentHistories).hasSize(1);
        assertThat(paymentHistories.get(0).getCode()).isEqualTo(ErrorCode.PAYMENT_INVALID_CUSTOMER.name());
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("주문이 주문시작 상태가 아니면 예외를 반환한다")
    void orderWithInvalidOrderStatus() {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        order.changeToOrderComplete();
        orderRepository.save(order);
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 10000L, "123"
        );

        assertThatThrownBy(
                () -> paymentFacade.payOrder(payOrderRequest)
        )
                .isInstanceOf(IllegalStatusException.class);

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAll();
        assertThat(paymentHistories).hasSize(1);
        assertThat(paymentHistories.get(0).getCode()).isEqualTo(ErrorCode.PAYMENT_ALREADY_FINISHED.name());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("같은 결제 요청을 동시에 100번하면 100번 모두 응답을 성공한다")
    void orderConccurencySucceed() throws InterruptedException {
        given(orderDataPlatformSendService.send(any())).willReturn(true);
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 10000L, "123"
        );

        final int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.payOrderRedis(payOrderRequest, "nothing");
                    success.incrementAndGet();
                } catch (InvalidParamException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 1,
                "반환된 1건은 분산환경에서 동시에 100건의 결제 시도 중 성공한 최초 시도이다");

        Assertions.assertEquals(fail.get(), 99,
                "반환된 99건은 분산환경에서 동시에 100건의 결제 시도 중 처리중이라는 예외이다");

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAll();
        Assertions.assertEquals(paymentHistories.size(), 1,
                "반환된 1건은 최초로 성공한 결제이력이다");

        OrderDataPlatformPayload orderDataPlatformPayload = OrderDataPlatformPayload.of(OrderDataPlatformEvent.of(order));
        verify(orderDataPlatformSendService, times(1)).send(orderDataPlatformPayload);
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @DisplayName("결제 요청의 마지막에 예외가 발생하면 데이터 플랫폼 전송 이벤트는 발생하지 않는다")
    void runtimeExceptionPreventDataPlatformSendEvent() throws InterruptedException {
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 10000L, "123"
        );

        final int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.payOrderRedis(payOrderRequest, "1");
                    success.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(fail.get(), 100,
                "반환된 100건은 분산환경에서 동시에 100건의 결제 시도 예외 건수이다");

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAll();
        Assertions.assertEquals(paymentHistories.size(), 0,
                "반환된 결제 이력 0건이다");

        OrderDataPlatformPayload orderDataPlatformPayload = OrderDataPlatformPayload.of(OrderDataPlatformEvent.of(order));
        verify(orderDataPlatformSendService, times(0)).send(orderDataPlatformPayload);
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    @DisplayName("데이터플랫폼 전송에 실패해도 기존 트랜잭션이 롤백되지 않는다")
    void dataPlatformExceptionDoesNotRollbackTransaction() throws InterruptedException {
        given(orderDataPlatformSendService.send(any())).willReturn(false);
        Customer customer = customerFixture();
        Point point = pointFixture(customer.getId(), 20000L);
        Order order = orderFixture(customer.getId());
        PaymentCommand.PayOrderRequest payOrderRequest = createPaymentRequest(
                order.getId(), customer.getId(), "TOSS", 10000L, "123"
        );

        final int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.payOrderRedis(payOrderRequest, "nothing");
                    success.incrementAndGet();
                } catch (InvalidParamException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 1,
                "반환된 1건은 분산환경에서 동시에 100건의 결제 시도 중 성공한 최초 시도이다");

        Assertions.assertEquals(fail.get(), 99,
                "반환된 99건은 분산환경에서 동시에 100건의 결제 시도 중 처리중이라는 예외이다");

        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAll();
        Assertions.assertEquals(paymentHistories.size(), 1,
                "반환된 1건은 최초로 성공한 결제이력이다");

        List<Payment> payments = paymentRepository.findAll();
        Assertions.assertEquals(payments.size(), 1,
                "반환된 1건은 최초로 성공한 결제이다");

        OrderDataPlatformPayload orderDataPlatformPayload = OrderDataPlatformPayload.of(OrderDataPlatformEvent.of(order));
        verify(orderDataPlatformSendService, times(2)).send(orderDataPlatformPayload);
    }

    //paymentIdempotency
    private PaymentIdempotency paymentIdempotencyFixture(Long orderId, String idempotencyKey) {
        PaymentIdempotency paymentIdempotency = createPaymentIdempotency(orderId, idempotencyKey);

        return paymentIdempotencyRepository.save(paymentIdempotency);
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

        return pointRepository.save(point);
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

        return customerRepository.save(customer);
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
                .build();
    }

    private OrderItemOption createOrderItemOption(OrderItem orderItem) {
        return OrderItemOption.builder()
                .orderItem(orderItem)
                .itemOptionPrice(0L)
                .build();
    }

    //payment
    private PaymentCommand.PayOrderRequest createPaymentRequest(
            Long orderId, Long customerId, String paymentMethod, Long amount, String idempotencyKey) {
        return PaymentCommand.PayOrderRequest.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .idempotencyKey(idempotencyKey)
                .build();
    }
}
