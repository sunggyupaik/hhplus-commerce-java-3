package com.hhplus.commerce.application.order;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderCommand;
import com.hhplus.commerce.domain.order.OrderInfo;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.interfaces.order.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("OrderCreateService 클래스")
class OrderCreateServiceTest {
    private OrderStore orderStore;
    private OrderCreateService orderCreateService;

    @BeforeEach
    void setUp() {
        orderStore = mock(OrderStore.class);
        orderCreateService = new OrderCreateService(orderStore);
    }

    @Nested
    @DisplayName("createOrder 메소드는")
    class Describe_createOrder {
        @Nested
        @DisplayName("만약 주문 정보가 주어진다면")
        class Context_with_order_request {
            private final Long createdOrderId = 1L;
            private final Long createdOrderItemId = 2L;
            private final Long createdOrderItemOptionId = 3L;

            @Test
            @DisplayName("주문을 생성하고 반환한다")
            void it_returns_created_order() {
                Order order = createOrderAggregate(createdOrderId, createdOrderItemId, createdOrderItemOptionId);
                OrderDto.OrderRequest orderRequest = createOrderRequest();

                given(orderStore.save(any(Order.class))).willReturn(order);

                var command = OrderCommand.OrderRequest.of(1L, orderRequest);
                OrderInfo.CreateResponse createResponse = orderCreateService.createOrder(command);

                assertThat(createResponse.getOrderId()).isEqualTo(createdOrderId);
                verify(orderStore, times(1)).save(any(Order.class));
                verify(orderStore, times(1)).saveOrderItem(any(OrderItem.class));
                verify(orderStore, times(1)).saveOrderItemOption(any(OrderItemOption.class));
            }
        }
    }

    private Order createOrderAggregate(Long orderId, Long orderItemId, Long orderItemOptionId) {
        Order order = createOrder(orderId);
        OrderItem orderItem = createOrderItem(orderItemId, order);
        OrderItemOption orderItemOption = createOrderItemOption(orderItemOptionId, orderItem);
        orderItem.changeOrderItemOption(orderItemOption);
        order.addOrderItem(orderItem);

        return order;
    }

    private Order createOrder(Long id) {
        return Order.builder()
                .id(id)
                .build();
    }

    private OrderItem createOrderItem(Long id, Order order) {
        return OrderItem.builder()
                .id(id)
                .order(order)
                .build();
    }

    private OrderItemOption createOrderItemOption(Long id, OrderItem orderItem) {
        return OrderItemOption.builder()
                .id(id)
                .orderItem(orderItem)
                .build();
    }

    private OrderDto.OrderRequest createOrderRequest() {
        OrderDto.OrderItemOptionRequest orderItemOptionRequest = OrderDto.OrderItemOptionRequest.builder().build();

        List<OrderDto.OrderItemRequest> orderItemRequestList =
                List.of(OrderDto.OrderItemRequest.builder().orderItemOption(orderItemOptionRequest).build());

        return OrderDto.OrderRequest.builder().orderItemList(orderItemRequestList).build();
    }
}
