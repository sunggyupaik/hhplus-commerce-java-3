package com.hhplus.commerce.application.order;

import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.ItemStore;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import com.hhplus.commerce.domain.order.dto.OrderRequest;
import com.hhplus.commerce.infra.item.ItemInventoryRepository;
import com.hhplus.commerce.infra.item.ItemOptionRepository;
import com.hhplus.commerce.infra.item.ItemRepository;
import com.hhplus.commerce.infra.order.OrderItemOptionRepository;
import com.hhplus.commerce.infra.order.OrderItemRepository;
import com.hhplus.commerce.infra.order.OrderRepository;
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
    @Autowired private  OrderFacade orderFacade;
    @Autowired private  ItemStore itemStore;
    @Autowired private  ItemReader itemReader;
    @Autowired private  OrderReader orderReader;

    //DB 초기화용
    @Autowired private  ItemRepository itemRepository;
    @Autowired private  ItemOptionRepository itemOptionRepository;
    @Autowired private  ItemInventoryRepository itemInventoryRepository;
    @Autowired private  OrderRepository orderRepository;
    @Autowired private  OrderItemRepository orderItemRepository;
    @Autowired private  OrderItemOptionRepository orderItemOptionRepository;

    @AfterEach
    void tearDown() {
        itemAggregateDeleteAllInBatch();
        orderAggregateDeleteAllInBatch();
    }

    private void orderAggregateDeleteAllInBatch() {
        orderItemOptionRepository.deleteAllInBatch();
        orderItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    private void itemAggregateDeleteAllInBatch() {
        itemInventoryRepository.deleteAllInBatch();
        itemOptionRepository.deleteAllInBatch();
        itemRepository.deleteAllInBatch();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("주어진 주문정보에 따라 재고를 차감하고 주문서를 생성한다")
    void order() {
        Item item = createItemAggregateFixture();
        OrderRequest orderRequest = createOrderRequest(item.getId());

        Order createdOrder = orderFacade.order(orderRequest);

        ItemInventory itemInventory = itemReader.getItemInventory(1L);
        Assertions.assertEquals(itemInventory.getQuantity(), 8,
                "10개 중 2개를 주문하면 재고는 8개가 남는다");
        Assertions.assertEquals(createdOrder.getId(), 1L,
                "새로운 주문서가 생성된다");
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("잔고 10개에서 동시에 2개씩 10번 주문을 신청하면 5번은 성공하고 5번은 재고 없음으로 실패한다.")
    void orderThrowsIllegalStatusException() throws InterruptedException {
        Item item = createItemAggregateFixture();

        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    OrderRequest orderRequest = createOrderRequest(item.getId());
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

        Assertions.assertEquals(success.get(), 5,
                "재고가 10개이므로 2개씩 5번 주문 가능하다");
        Assertions.assertEquals(fail.get(), 5,
                "재고가 10개이므로 초과 주문 5번은 예외를 반환한다");
    }

    //item
    private Item createItemAggregateFixture() {
        Item item = createItem();
        ItemOption itemOption = createItemOption(item);
        ItemInventory itemInventory = createItemInventory(itemOption, 10L);
        item.addItemOption(itemOption);
        itemOption.changeInventory(itemInventory);

        Item createdItem = itemStore.saveItem(item);
        ItemOption createdItemOption = itemStore.saveItemOption(itemOption);
        ItemInventory createdItemInventory = itemStore.saveItemInventory(itemInventory);

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

    private OrderRequest createOrderRequest(Long itemId) {
        OrderRequest.OrderItemOptionRequest orderItemOptionRequest =
                OrderRequest.OrderItemOptionRequest
                        .builder()
                        .itemOptionId(itemId)
                        .build();

        List<OrderRequest.OrderItemRequest> orderItemRequestList =
                List.of(
                        OrderRequest.OrderItemRequest.builder()
                            .orderItemOptionRequest(orderItemOptionRequest)
                            .itemId(itemId)
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
