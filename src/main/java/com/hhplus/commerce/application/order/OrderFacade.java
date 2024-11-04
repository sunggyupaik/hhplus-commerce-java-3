package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.item.ItemStockService;
import com.hhplus.commerce.application.order.dto.OrderRequest;
import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.hhplus.commerce.domain.order.Order.PAY_CHECK_MINUTE;

@Service
@RequiredArgsConstructor
public class OrderFacade {
    private final ItemStockService itemStockService;
    private final OrderCreateService orderCreateService;
    private final OrderQueryService orderQueryService;
    private final OrderCancelHandler orderCancelHandler;

    @Transactional
    public Order orderPessimisticLock(Long customerId, OrderRequest request) {
        // 재고 차감(itemId 오름차순)
        request.getOrderItemRequestList().stream()
                .sorted(Comparator.comparing(OrderRequest.OrderItemRequest::getItemId))
                .forEach(orderItemRequest -> {
                    OrderRequest.OrderItemOptionRequest orderItemOptionRequest
                            = orderItemRequest.getOrderItemOptionRequest();

                    itemStockService.decreaseStockPessimistic(
                            orderItemOptionRequest.getItemOptionId(),
                            Long.valueOf(orderItemRequest.getOrderCount())
                    );
                });

        //주문 저장
        return orderCreateService.createOrder(customerId, request);
    }

    @Transactional
    public Order orderOptimisticLock(Long customerId, OrderRequest request) {
        // 재고 차감
        request.getOrderItemRequestList().stream()
                .sorted(Comparator.comparing(OrderRequest.OrderItemRequest::getItemId))
                .forEach(orderItemRequest -> {
                    OrderRequest.OrderItemOptionRequest orderItemOptionRequest
                            = orderItemRequest.getOrderItemOptionRequest();

                    itemStockService.decreaseStockPessimistic(
                            orderItemOptionRequest.getItemOptionId(),
                            Long.valueOf(orderItemRequest.getOrderCount())
                    );
                });

        //주문 저장
        return orderCreateService.createOrder(customerId, request);
    }

    @Transactional
    public Order orderDistributedLock(Long customerId, OrderRequest request) {
        // 재고 차감
        request.getOrderItemRequestList().stream()
                .sorted(Comparator.comparing(OrderRequest.OrderItemRequest::getItemId))
                .forEach(orderItemRequest -> {
                    OrderRequest.OrderItemOptionRequest orderItemOptionRequest
                            = orderItemRequest.getOrderItemOptionRequest();

                    itemStockService.decreaseStockPessimistic(
                            orderItemOptionRequest.getItemOptionId(),
                            Long.valueOf(orderItemRequest.getOrderCount())
                    );
                });

        //주문 저장
        return orderCreateService.createOrder(customerId, request);
    }

    //@Scheduled(cron = "0 * * * * *")
    @Transactional
    public void orderCancelBatch() {
        List<Order> orders = orderQueryService.getInitOrders();

        orders.forEach(order -> {
            orderCancelHandler.cancelOrder(order, order.getCreatedDate(), PAY_CHECK_MINUTE);
        });
    }

    @Transactional
    public void orderCancelWithTime(int minute, LocalDateTime dateTime) {
        List<Order> orders = orderQueryService.getInitOrders();

        orders.forEach(order -> {
            orderCancelHandler.cancelOrder(order, dateTime, minute);
        });
    }
}
