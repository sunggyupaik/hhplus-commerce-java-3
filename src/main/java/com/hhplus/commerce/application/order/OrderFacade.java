package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.item.ItemStockService;
import com.hhplus.commerce.application.order.kafkaSample.OrderSampleKafkaService;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderCommand;
import com.hhplus.commerce.domain.order.OrderInfo;
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
    private final OrderSampleKafkaService orderSampleKafkaService;

    @Transactional
    public OrderInfo.CreateResponse orderPessimisticLock(OrderCommand.OrderRequest request) {
        // 재고 차감(itemId 오름차순)
        request.getOrderItemList().stream()
                .sorted(Comparator.comparing(OrderCommand.OrderItemRequest::getItemId))
                .forEach(orderItemRequest -> {
                    var orderItemOption = orderItemRequest.getOrderItemOption();

                    itemStockService.decreaseStockPessimistic(
                            orderItemOption.getItemOptionId(),
                            Long.valueOf(orderItemRequest.getOrderCount())
                    );
                });

        //주문 저장
        return orderCreateService.createOrder(request);
    }

    @Transactional
    public OrderInfo.CreateResponse orderOptimisticLock(OrderCommand.OrderRequest request) {
        // 재고 차감
        request.getOrderItemList().stream()
                .sorted(Comparator.comparing(OrderCommand.OrderItemRequest::getItemId))
                .forEach(orderItemRequest -> {
                    var orderItemOption = orderItemRequest.getOrderItemOption();

                    itemStockService.decreaseStockPessimistic(
                            orderItemOption.getItemOptionId(),
                            Long.valueOf(orderItemRequest.getOrderCount())
                    );
                });

        //주문 저장
        return orderCreateService.createOrder(request);
    }

    @Transactional
    public OrderInfo.CreateResponse orderDistributedLock(OrderCommand.OrderRequest request) {
        // 재고 차감
        request.getOrderItemList().stream()
                .sorted(Comparator.comparing(OrderCommand.OrderItemRequest::getItemId))
                .forEach(orderItemRequest -> {
                    var orderItemOption = orderItemRequest.getOrderItemOption();

                    itemStockService.decreaseStockPessimistic(
                            orderItemOption.getItemOptionId(),
                            Long.valueOf(orderItemRequest.getOrderCount())
                    );
                });

        //주문 저장
        return orderCreateService.createOrder(request);
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

    @Transactional(readOnly = true)
    public List<OrderInfo.DetailResponse> getOrders(Long customerId) {
        return orderQueryService.getDetailOrders(customerId);
    }

    @Transactional
    public void sample(Long id) {
        orderSampleKafkaService.sample(id);
    }
}
