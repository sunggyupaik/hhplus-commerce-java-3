package com.hhplus.commerce.application.item;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@TearDownDatabase
public class ItemStockIntegrationTest {
    @Autowired private ItemStockService itemStockService;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemOptionRepository itemOptionRepository;
    @Autowired private ItemInventoryRepository itemInventoryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private OrderItemOptionRepository orderItemOptionRepository;

    @Test
    @DisplayName("10개의 상품을 동시에 1개씩 총 10번 차감하면 재고는 0개이다.")
    void concurrentDecreaseForSamePoint10times() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Item item = createItemAggregateFixture(10L);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    itemStockService.decreaseStockPessimistic(item.getId(), 1L);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long quantity = itemInventoryRepository.findById(item.getId()).orElseThrow().getQuantity();
        assertThat(10).isEqualTo(success.get());
        assertThat(0L).isEqualTo(quantity);
    }

    @Test
    @DisplayName("10개의 상품을 동시에 1개씩 총 10번 증가하면 재고는 20개이다.")
    void concurrentIncreaseForSamePoint10times() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Item item = createItemAggregateFixture(10L);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    itemStockService.increaseStock(item.getId(), 1L);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long quantity = itemInventoryRepository.findById(item.getId()).orElseThrow().getQuantity();
        assertThat(10).isEqualTo(success.get());
        assertThat(20L).isEqualTo(quantity);
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
