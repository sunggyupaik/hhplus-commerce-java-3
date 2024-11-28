package com.hhplus.commerce.application.item;

import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.ItemStore;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventoryHistory;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.support.distributedLock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemStockService {
    private final ItemReader itemReader;
    private final ItemStore itemStore;

    @Transactional
    public Long decreaseStockPessimistic(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventoryWithPessimisticLock(itemOptionId);
        Long decreasedQuantity = itemInventory.decreaseStock(quantity);
        itemStore.createItemInventoryHistory(ItemInventoryHistory.createMinus(itemOptionId, quantity));

        return decreasedQuantity;
    }

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public Long decreaseStockOptimisticLock(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventoryWithOptimisticLock(itemOptionId);
        Long decreasedQuantity = itemInventory.decreaseStock(quantity);
        itemStore.createItemInventoryHistory(ItemInventoryHistory.createMinus(itemOptionId, quantity));

        return decreasedQuantity;
    }

    @DistributedLock(key = "'item'.concat(':').concat(#itemOptionId)")
    public Long decreaseStockDistributedLock(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventory(itemOptionId);
        Long decreasedQuantity = itemInventory.decreaseStock(quantity);
        itemStore.createItemInventoryHistory(ItemInventoryHistory.createMinus(itemOptionId, quantity));

        return decreasedQuantity;
    }

    @Transactional
    public Long increaseStock(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventoryWithPessimisticLock(itemOptionId);
        Long increasedQuantity = itemInventory.increaseStock(quantity);
        itemStore.createItemInventoryHistory(ItemInventoryHistory.createPlus(itemOptionId, quantity));

        return increasedQuantity;
    }

    @Transactional
    public void increaseStockWithTime(Order order, LocalDateTime dateTime, int minute) {
        LocalDateTime orderCreatedTime = order.getCreatedDate();

        if (orderCreatedTime.plusMinutes(minute).isAfter(dateTime)) {
            List<OrderItem> orderItems = order.getOrderItems();

            orderItems.forEach(orderItem -> {
                ItemInventory itemInventory = itemReader.getItemInventoryWithPessimisticLock(orderItem.getItemId());
                itemInventory.increaseStock(Long.valueOf(orderItem.getOrderCount()));
                itemStore.createItemInventoryHistory(
                        ItemInventoryHistory.createPlus(
                                itemInventory.getItemOption().getId(),
                                Long.valueOf(orderItem.getOrderCount())
                        )
                );
            });
        }
    }
}
