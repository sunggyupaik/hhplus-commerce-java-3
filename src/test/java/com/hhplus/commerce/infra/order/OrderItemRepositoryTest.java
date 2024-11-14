package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerStore;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TearDownDatabase
class OrderItemRepositoryTest {
    @Autowired private CustomerStore customerStore;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private OrderItemOptionRepository orderItemOptionRepository;

    @Test
    void testFindTop5ByOrderCountSum() {
        Customer customer = customerFixture();
        orderFixture(customer.getId(), 1, 1L);
        orderFixture(customer.getId(), 2, 2L);
        orderFixture(customer.getId(), 3, 3L);
        orderFixture(customer.getId(), 4, 4L);
        orderFixture(customer.getId(), 5, 5L);
        orderFixture(customer.getId(), 6, 6L);
        orderFixture(customer.getId(), 7, 1L);
        orderFixture(customer.getId(), 8, 2L);

        /*
        * 1. 2L -> 10
        * 2. 1L -> 8
        * 3. 6L -> 6
        * 4. 5L -> 5
        * 5. 4L -> 4
        * */

        LocalDateTime startTime = LocalDateTime.now().minusDays(3);
        LocalDateTime endTime = LocalDateTime.now();

        List<ItemBestResponse> top5ByOrderCountSum
                = orderItemRepository.findTop5ByOrderCountSum(startTime, endTime);

        assertThat(top5ByOrderCountSum).hasSize(5)
                .extracting("itemId", "count")
                .containsExactly(
                        Tuple.tuple(2L, 10L),
                        Tuple.tuple(1L, 8L),
                        Tuple.tuple(6L, 6L),
                        Tuple.tuple(5L, 5L),
                        Tuple.tuple(4L, 4L)
                );
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
    private Order orderFixture(Long customerId, Integer orderCount, Long itemId) {
        Order order = createOrder(customerId);
        OrderItem orderItem = createOrderItem(orderCount, order, itemId);
        OrderItemOption orderItemOption = createOrderItemOption(orderItem);

        orderRepository.save(order);
        orderItemRepository.save(orderItem);
        orderItemOptionRepository.save(orderItemOption);

        return order;
    }

    private Order createOrder(Long customerId) {
        return Order.builder()
                .customerId(customerId)
                .build();
    }

    private OrderItem createOrderItem(Integer orderCount, Order order, Long itemId) {
        return OrderItem.builder()
                .itemId(itemId)
                .orderCount(orderCount)
                .order(order)
                .itemPrice(5000L)
                .orderCount(orderCount)
                .build();
    }

    private OrderItemOption createOrderItemOption(OrderItem orderItem) {
        return OrderItemOption.builder()
                .orderItem(orderItem)
                .itemOptionPrice(0L)
                .build();
    }
}