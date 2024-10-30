package com.hhplus.commerce.application.item;

import com.hhplus.commerce.domain.Item.ItemReader;
import com.hhplus.commerce.domain.Item.itemInventory.ItemInventory;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import com.hhplus.commerce.domain.order.item.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemStockService {
    private final ItemReader itemReader;
    private final OrderReader orderReader;

    @Transactional
    public Long decreaseStock(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventoryWithPessimisticLock(itemOptionId);
        Long decreasedQuantity = itemInventory.decreaseStock(quantity);

        return decreasedQuantity;
    }

    @Transactional
    public void increaseStockWithTime(int minute, LocalDateTime now) {
        List<Order> orders = orderReader.getInitOrders();

        orders.forEach(order -> {
            LocalDateTime orderCreatedTime = order.getCreatedDate();
            if (orderCreatedTime.plusMinutes(minute).isAfter(now)) {
                List<OrderItem> orderItems = order.getOrderItems();

                orderItems.forEach(orderItem -> {
                    increaseStock(orderItem.getItemId(), Long.valueOf(orderItem.getOrderCount()));
                });
            }
        });
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void increaseStockBatch() {
        List<Order> orders = orderReader.getInitOrders();

        orders.forEach(order -> {
            LocalDateTime orderCreatedTime = order.getCreatedDate();
            if (orderCreatedTime.plusMinutes(15).isAfter(LocalDateTime.now())) {
                List<OrderItem> orderItems = order.getOrderItems();

                orderItems.forEach(orderItem -> {
                    increaseStockWithRequiresNew(orderItem.getItemId(), Long.valueOf(orderItem.getOrderCount()));
                });
            }
        });
    }

    @Transactional
    public Long increaseStock(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventoryWithPessimisticLock(itemOptionId);
        Long increasedQuantity = itemInventory.increaseStock(quantity);

        return increasedQuantity;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long increaseStockWithRequiresNew(Long itemOptionId, Long quantity) {
        ItemInventory itemInventory = itemReader.getItemInventoryWithPessimisticLock(itemOptionId);
        Long increasedQuantity = itemInventory.increaseStock(quantity);

        return increasedQuantity;
    }
}
