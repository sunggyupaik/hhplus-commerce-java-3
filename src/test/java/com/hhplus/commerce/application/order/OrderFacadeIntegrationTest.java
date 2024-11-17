package com.hhplus.commerce.application.order;

import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import com.hhplus.commerce.domain.Item.Item;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemOption.ItemOption;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderCommand;
import com.hhplus.commerce.infra.item.ItemInventoryRepository;
import com.hhplus.commerce.infra.item.ItemOptionRepository;
import com.hhplus.commerce.infra.item.ItemRepository;
import com.hhplus.commerce.infra.order.OrderRepository;
import com.hhplus.commerce.interfaces.order.OrderDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@TearDownDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderFacadeIntegrationTest {
    @Autowired private  OrderFacade orderFacade;

    @Autowired private  ItemRepository itemRepository;
    @Autowired private  ItemOptionRepository itemOptionRepository;
    @Autowired private  ItemInventoryRepository itemInventoryRepository;
    @Autowired private  OrderRepository orderRepository;

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("주어진 주문정보에 따라 재고를 차감하고 주문서를 생성한다")
    void order() {
        Item item = createItemAggregateFixture();
        OrderDto.OrderRequest orderRequest = createOrderRequest(item.getId());

        var command = OrderCommand.OrderRequest.of(1L, orderRequest);
        orderFacade.orderPessimisticLock(command);

        ItemInventory itemInventory = itemInventoryRepository.findById(item.getId()).orElseThrow();
        Assertions.assertEquals(8, itemInventory.getQuantity(),
                "10개 중 2개를 주문하면 재고는 8개가 남는다");

        List<Order> all = orderRepository.findAll();
        Assertions.assertEquals(all.size(), 1,
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
                    OrderDto.OrderRequest orderRequest = createOrderRequest(item.getId());
                    var command = OrderCommand.OrderRequest.of(1L, orderRequest);
                    orderFacade.orderPessimisticLock(command);
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

    private OrderDto.OrderRequest createOrderRequest(Long itemId) {
        OrderDto.OrderItemOptionRequest orderItemOptionRequest =
                OrderDto.OrderItemOptionRequest
                        .builder()
                        .itemOptionId(itemId)
                        .build();

        List<OrderDto.OrderItemRequest> orderItemRequestList =
                List.of(
                        OrderDto.OrderItemRequest.builder()
                                .orderItemOption(orderItemOptionRequest)
                                .itemId(itemId)
                                .orderCount(2)
                                .itemPrice(1000L)
                                .build()
                );

        return OrderDto.OrderRequest.builder()
                .orderItemList(orderItemRequestList)
                .customerId(1L)
                .build();
    }

    private OrderCommand.OrderRequest createOrderRequest(List<Long> ids) {
        OrderCommand.OrderItemOptionRequest orderItemOptionRequest =
                OrderCommand.OrderItemOptionRequest
                        .builder()
                        .itemOptionId(ids.get(0))
                        .build();

        List<OrderCommand.OrderItemRequest> orderItemRequestList =
                List.of(
                        OrderCommand.OrderItemRequest.builder()
                            .orderItemOption(orderItemOptionRequest)
                            .itemId(ids.get(0))
                            .orderCount(3)
                            .itemPrice(1000L)
                            .build(),
                        OrderCommand.OrderItemRequest.builder()
                                .orderItemOption(orderItemOptionRequest)
                                .itemId(ids.get(1))
                                .orderCount(3)
                                .itemPrice(1000L)
                                .build(),
                        OrderCommand.OrderItemRequest.builder()
                                .orderItemOption(orderItemOptionRequest)
                                .itemId(ids.get(2))
                                .orderCount(3)
                                .itemPrice(1000L)
                                .build()
                );

        return OrderCommand.OrderRequest.builder()
                .orderItemList(orderItemRequestList)
                .customerId(1L)
                .build();
    }
}
