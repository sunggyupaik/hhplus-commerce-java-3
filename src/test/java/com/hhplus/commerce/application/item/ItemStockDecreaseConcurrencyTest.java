package com.hhplus.commerce.application.item;

import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.infra.item.ItemInventoryRepository;
import com.hhplus.commerce.infra.item.ItemOptionRepository;
import com.hhplus.commerce.infra.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemStockDecreaseConcurrencyTest {
    @Autowired private ItemStockService itemStockService;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemOptionRepository itemOptionRepository;
    @Autowired private ItemInventoryRepository itemInventoryRepository;

    @BeforeEach
    void tearDown() {
        itemInventoryRepository.deleteAllInBatch();
        itemOptionRepository.deleteAllInBatch();
        itemRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("10개의 상품을 동시에 1개씩 총 10번 차감하면 재고는 0개이다.")
    void concurrentDecreaseForSamePoint10times() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Item item = createItemAggregateFixture();

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    itemStockService.decreaseStock(1L, 1L);
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

    //item
    private Item createItemAggregateFixture() {
        Item item = createItem();
        ItemOption itemOption = createItemOption(item);
        ItemInventory itemInventory = createItemInventory(itemOption, 10L);

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
}
