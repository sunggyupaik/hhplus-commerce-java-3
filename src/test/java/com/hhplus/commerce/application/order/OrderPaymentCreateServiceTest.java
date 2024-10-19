package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.OrderPayment;
import com.hhplus.commerce.domain.order.payment.OrderPaymentHistory;
import com.hhplus.commerce.domain.order.payment.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("PaymentCreateService 클래스")
class OrderPaymentCreateServiceTest {
    private PaymentCreateService paymentCreateService;
    private OrderStore orderStore;

    @BeforeEach
    void setUp() {
        orderStore = mock(OrderStore.class);
        paymentCreateService = new PaymentCreateService(orderStore);
    }

    @Nested
    @DisplayName("createPayment 메소드는")
    class Describe_createOrderPayment {
        private final Long ORDER_ID = 1L;
        private final Long ORDER_ITEM_ID = 2L;
        private final Long ORDER_ITEM_OPTION_ID = 3L;
        private final Long PAYMENT_ID = 4L;
        private final Long CUSTOMER_ID_1 = 5L;
        private final Long CUSTOMER_ID_2 = 6L;
        private final Long PAYMENT_HISTORY_ID = 6L;

        Order order = createOrder(ORDER_ID, CUSTOMER_ID_1);
        OrderItem orderItem = createOrderItem(ORDER_ITEM_ID, order, 5, 1000L);
        OrderItemOption orderItemOption = createOrderItemOption(ORDER_ITEM_OPTION_ID, orderItem, 1000L);
        Order orderAggregate = createOrderAggregate(order, orderItem, orderItemOption);
        OrderPayment orderPayment = createPayment(PAYMENT_ID, orderAggregate, CUSTOMER_ID_1, "TOSS", 10000L);
        OrderPaymentHistory orderPaymentHistory = createPaymentHistory(PAYMENT_HISTORY_ID, orderAggregate, "SUCCESS", "SUCCESS");
        PaymentRequest paymentRequest = createPaymentRequest(PAYMENT_ID, CUSTOMER_ID_1, "TOSS", 10000L);

        PaymentRequest invalidAmountRequest = createPaymentRequest(PAYMENT_ID, CUSTOMER_ID_1, "TOSS", 20000L);

        Order order_2 = createOrder(ORDER_ID, CUSTOMER_ID_2);
        Order orderAggregate_2 = createOrderAggregate(order_2, orderItem, orderItemOption);

        @Nested
        @DisplayName("만약 존재하는 주문과 결제 정보가 주어진다면")
        class Context_with_existed_order_and_payment_request {
            @Test
            @DisplayName("결제, 결제이력 생성 후 결제 식별자를 반환한다.")
            void it_returns_created_payment_id() {
                given(orderStore.savePayment(any(OrderPayment.class))).willReturn(orderPayment);
                given(orderStore.saveOrderPaymentHistory(any(OrderPaymentHistory.class))).willReturn(orderPaymentHistory);

                Long createdPaymentId = paymentCreateService.createPayment(orderAggregate, paymentRequest);

                assertThat(createdPaymentId).isEqualTo(PAYMENT_ID);
                verify(orderStore, times(1)).saveOrderPaymentHistory(any(OrderPaymentHistory.class));
            }
        }

        @Nested
        @DisplayName("만약 주문자와 결제자가 다르다면")
        class Context_with_order_payment_customer_different {
            @Test
            @DisplayName("주문자와 결제자가 다르다는 예외를 반환한다")
            void it_throws_customer_invalid() {
                assertThatThrownBy(
                        () -> paymentCreateService.createPayment(orderAggregate_2, paymentRequest)
                )
                        .isInstanceOf(InvalidParamException.class);

                verify(orderStore, times(1))
                        .saveOrderPaymentHistory(any(OrderPaymentHistory.class));
            }
        }

        @Nested
        @DisplayName("만약 주문서 금액과 결제 요청 금액이 다르다면")
        class Context_with_not_valid_amount {
            @Test
            @DisplayName("금액이 다르다는 예외를 반환한다")
            void it_throws_amounts_invalid() {
                assertThatThrownBy(
                        () -> paymentCreateService.createPayment(orderAggregate, invalidAmountRequest)
                )
                        .isInstanceOf(InvalidParamException.class);

                verify(orderStore, times(1))
                        .saveOrderPaymentHistory(any(OrderPaymentHistory.class));
            }
        }

        @Nested
        @DisplayName("만약 주문이 주문 시작 상태가 아니라면")
        class Context_with_not_valid_order_status {
            @Test
            @DisplayName("주문 상태가 정상이 아니라는 예외를 반환한다")
            void it_throws_amounts_invalid() {
                orderAggregate.changeToOrderComplete();
                assertThatThrownBy(
                        () -> paymentCreateService.createPayment(orderAggregate, invalidAmountRequest)
                )
                        .isInstanceOf(InvalidParamException.class);

                verify(orderStore, times(1))
                        .saveOrderPaymentHistory(any(OrderPaymentHistory.class));
            }
        }
    }

    private Order createOrderAggregate(Order order, OrderItem orderItem, OrderItemOption orderItemOption) {
        orderItem.changeOrderItemOption(orderItemOption);
        order.addOrderItem(orderItem);

        return order;
    }

    private Order createOrder(Long id, Long customerId) {
        return Order.builder()
                .id(id)
                .customerId(customerId)
                .build();
    }

    private OrderItem createOrderItem(Long id, Order order, Integer orderCount, Long itemPrice) {
        return OrderItem.builder()
                .id(id)
                .order(order)
                .orderCount(orderCount)
                .itemPrice(itemPrice)
                .build();
    }

    private OrderItemOption createOrderItemOption(Long id, OrderItem orderItem, Long itemOptionPrice) {
        return OrderItemOption.builder()
                .id(id)
                .orderItem(orderItem)
                .itemOptionPrice(itemOptionPrice)
                .build();
    }

    private OrderPaymentHistory createPaymentHistory(Long id, Order order, String code, String message) {
        return OrderPaymentHistory.builder()
                .id(id)
                .order(order)
                .code(code)
                .message(message)
                .build();
    }

    private OrderPayment createPayment(Long id, Order order, Long customerId, String paymentMethod, Long amount) {
        return OrderPayment.builder()
                .id(id)
                .order(order)
                .customerId(customerId)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .amount(amount)
                .build();
    }

    private PaymentRequest createPaymentRequest(Long orderId, Long customerId, String paymentMethod, Long amount) {
        return PaymentRequest.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .build();
    }
}