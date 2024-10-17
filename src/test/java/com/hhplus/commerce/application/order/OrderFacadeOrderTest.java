package com.hhplus.commerce.application.order;

import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.ItemStore;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.dto.OrderRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderFacadeOrderTest {
    private final OrderFacade orderFacade;
    private final ItemStore itemStore;
    private final ItemReader itemReader;
    private final OrderStore orderStore;

    public OrderFacadeOrderTest(
            @Autowired OrderFacade orderFacade,
            @Autowired ItemStore itemStore,
            @Autowired ItemReader itemReader,
            @Autowired OrderStore orderStore
    ) {
        this.orderFacade = orderFacade;
        this.itemStore = itemStore;
        this.itemReader = itemReader;
        this.orderStore = orderStore;
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("주어진 주문정보에 따라 재고를 차감하고 주문서를 생성한다")
    void order() {
        itemFixture();
        OrderRequest orderRequest = createOrderRequest();

        Order createdOrder = orderFacade.order(orderRequest);

        ItemInventory itemInventory = itemReader.getItemInventory(1L);
        Assertions.assertEquals(itemInventory.getQuantity(), 8, "10개 중 2개를 주문하면 재고는 8개가 남는다.");
        Assertions.assertEquals(createdOrder.getId(), 1L, "새로운 주문서가 생성됩니다.");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("잔고 8개에서 동시에 2개씩 10번 주문을 신청하면 4번은 성공하고 6번은 재고 없음으로 실패한다.")
    void orderThrowsIllegaStatusException() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    OrderRequest orderRequest = createOrderRequest();
                    Order createdOrder = orderFacade.order(orderRequest);
                    success.incrementAndGet();
                } catch (IllegalStatusException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 4, "재고가 8개이므로 2개씩 4번 주문 가능하다");
        Assertions.assertEquals(fail.get(), 6, "재고가 8개이므로 초과 주문은 예외를 반환한다");
    }

    //item
    private Item itemFixture() {
        Item item = createItem(null);
        ItemOption itemOption = createItemOption(null, item);
        ItemInventory itemInventory = createItemInventory(null, itemOption, 10L);
        item.addItemOption(itemOption);
        itemOption.changeInventory(itemInventory);

        Item createdItem = itemStore.saveItem(item);
        ItemOption createdItemOption = itemStore.saveItemOption(itemOption);
        ItemInventory createdItemInventory = itemStore.saveItemInventory(itemInventory);

        return item;
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

    private OrderRequest createOrderRequest() {
        OrderRequest.OrderItemOptionRequest orderItemOptionRequest =
                OrderRequest.OrderItemOptionRequest
                        .builder()
                        .itemOptionId(1L)
                        .build();

        List<OrderRequest.OrderItemRequest> orderItemRequestList =
                List.of(
                        OrderRequest.OrderItemRequest.builder()
                            .orderItemOptionRequest(orderItemOptionRequest)
                            .itemId(1L)
                            .orderCount(2)
                            .itemPrice(1000L)
                            .build()
                );

        return OrderRequest.builder()
                .orderItemRequestList(orderItemRequestList)
                .customerId(1L)
                .build();
    }
}
