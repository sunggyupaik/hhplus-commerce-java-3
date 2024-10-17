package com.hhplus.commerce.application.item;

import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.ItemStore;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
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
    private final ItemStockDecreaseService itemStockDecreaseService;
    private final ItemStore itemStore;
    private final ItemReader itemReader;

    public ItemStockDecreaseConcurrencyTest(
            @Autowired ItemStore itemStore,
            @Autowired ItemStockDecreaseService itemStockDecreaseService,
            @Autowired ItemReader itemReader
    ) {
        this.itemStore = itemStore;
        this.itemStockDecreaseService = itemStockDecreaseService;
        this.itemReader = itemReader;
    }

    @BeforeEach
    void setUp() {
        Item item = createItem(null);
        ItemOption itemOption = createItemOption(null, item);
        ItemInventory itemInventory = createItemInventory(null, itemOption, 10L);
        item.addItemOption(itemOption);
        itemOption.changeInventory(itemInventory);

        Item createdItem = itemStore.saveItem(item);
        ItemOption createdItemOption = itemStore.saveItemOption(itemOption);
        ItemInventory createdItemInventory = itemStore.saveItemInventory(itemInventory);
    }

    @Test
    @DisplayName("10개의 상품을 동시에 1개씩 총 10번 차감하면 재고는 0개이다.")
    void concurrentDecreaseForSamePoint10times() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    itemStockDecreaseService.decreaseStock(1L, 1L);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(success.get()).isEqualTo(10);
        assertThat(itemReader.getItemInventory(1L).getQuantity()).isEqualTo(0);
    }

    private Item createItem(Long id) {
        return Item.builder()
                .id(id)
                .build();
    }

    private ItemOption createItemOption(Long id, Item item) {
        return ItemOption.builder()
                .id(id)
                .item(item)
                .build();
    }

    private ItemInventory createItemInventory(Long id, ItemOption itemOption, Long quantity) {
        return ItemInventory.builder()
                .id(id)
                .itemOption(itemOption)
                .quantity(quantity)
                .build();
    }
}
