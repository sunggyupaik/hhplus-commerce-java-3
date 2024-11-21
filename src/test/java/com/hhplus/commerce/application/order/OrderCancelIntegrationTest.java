package com.hhplus.commerce.application.order;

import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.infra.item.ItemInventoryRepository;
import com.hhplus.commerce.infra.item.ItemOptionRepository;
import com.hhplus.commerce.infra.item.ItemRepository;
import com.hhplus.commerce.infra.order.OrderItemOptionRepository;
import com.hhplus.commerce.infra.order.OrderItemRepository;
import com.hhplus.commerce.infra.order.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@TearDownDatabase
public class OrderCancelIntegrationTest {
    @Autowired private OrderFacade orderFacade;

    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemOptionRepository itemOptionRepository;
    @Autowired private ItemInventoryRepository itemInventoryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private OrderItemOptionRepository orderItemOptionRepository;

    @Test
    @DisplayName("주문완료 후 15분 후에도 결제가 없다면 재고를 원복한다")
    void integrationBatch() {
        Item item = createItemAggregateFixture(10L);
        Order order = orderFixture(1L, 2, item.getId());

        orderFacade.orderCancelWithParam(15, order.getCreatedDate().plusMinutes(10));

        Long quantity = itemInventoryRepository.findById(item.getId()).orElseThrow().getQuantity();
        Assertions.assertEquals(quantity, 12L,
                "반환된 12개는 기존 10개와 재고 복구로 원복된 2개의 합이다");
    }

    @Test
    @DisplayName("주문 취소를 동시에 5번해도 한번만 성공한다")
    void concurrentIncreaseForSamePoint10times() throws InterruptedException {
        final int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Item item = createItemAggregateFixture(10L);
        Order order = orderFixture(1L, 2, item.getId());

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderFacade.orderCancelWithParam(15, order.getCreatedDate().plusMinutes(10));
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long quantity = itemInventoryRepository.findById(item.getId()).orElseThrow().getQuantity();
        Assertions.assertEquals(quantity, 12L,
                "반환된 12개는 기존 10개와 재고 복구로 원복된 2개의 합이다");
    }

    //item
    private Item createItemAggregateFixture(Long quantity) {
        Item item = createItem();
        ItemOption itemOption = createItemOption(item);
        ItemInventory itemInventory = createItemInventory(itemOption, quantity);

        itemRepository.save(item);
        itemOptionRepository.save(itemOption);
        itemInventoryRepository.save(itemInventory);

        return item;
    }

    private Item createItem() {
        return Item.builder()
                .build();
    }

    private ItemOption createItemOption(Item item) {
        return ItemOption.builder()
                .item(item)
                .build();
    }

    private ItemInventory createItemInventory(ItemOption itemOption, Long quantity) {
        return ItemInventory.builder()
                .itemOption(itemOption)
                .quantity(quantity)
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
