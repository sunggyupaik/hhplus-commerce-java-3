package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.domain.order.payment.Payment;
import com.hhplus.commerce.domain.order.payment.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("PaymentCreateService 클래스")
class PaymentCreateServiceTest {
    private PaymentCreateService paymentCreateService;
    private OrderStore orderStore;

    @BeforeEach
    void setUp() {
        orderStore = mock(OrderStore.class);
        paymentCreateService = new PaymentCreateService(orderStore);
    }

    @Nested
    @DisplayName("createPayment 메소드는")
    class Describe_createPayment {
        private final Long existedOrderId = 1L;
        private final Long existedOrderItemId = 2L;
        private final Long existedOrderItemOptionId = 3L;
        private final Long existedPaymentId = 4L;
        private final Long existedCustomerId_1 = 5L;
        private final Long existedCustomerId_2 = 6L;

        Order order = createOrder(existedOrderId, existedCustomerId_1);
        OrderItem orderItem = createOrderItem(existedOrderItemId, order, 5, 1000L);
        OrderItemOption orderItemOption = createOrderItemOption(existedOrderItemOptionId, orderItem, 1000L);
        Order orderAggregate = createOrderAggregate(order, orderItem, orderItemOption);
        Payment payment = createPayment(existedPaymentId, orderAggregate, existedCustomerId_1, "TOSS", 10000L);
        PaymentRequest paymentRequest = createPaymentRequest(existedPaymentId, existedCustomerId_1, "TOSS", 10000L);

        Payment invalidAmountPayment = createPayment(existedPaymentId, orderAggregate, existedCustomerId_1, "TOSS", 10000L);
        PaymentRequest invalidAmountRequest = createPaymentRequest(existedPaymentId, existedCustomerId_1, "TOSS", 20000L);

        Order order_2 = createOrder(existedOrderId, existedCustomerId_2);
        Order orderAggregate_2 = createOrderAggregate(order_2, orderItem, orderItemOption);

        @Nested
        @DisplayName("만약 존재하는 주문과 결제 정보가 주어진다면")
        class Context_with_existed_order_and_payment_request {
            @Test
            @DisplayName("결제하고 결제 식별자를 반환한다.")
            void it_creates_payment_and_returns_payment_id() {

                given(orderStore.savePayment(any(Payment.class))).willReturn(payment);

                Long createdPaymentId = paymentCreateService.createPayment(orderAggregate, paymentRequest);

                assertThat(createdPaymentId).isEqualTo(existedPaymentId);
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
                .oderItem(orderItem)
                .itemOptionPrice(itemOptionPrice)
                .build();
    }

    private Payment createPayment(Long id, Order order, Long customerId, String paymentMethod, Long amount) {
        return Payment.builder()
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