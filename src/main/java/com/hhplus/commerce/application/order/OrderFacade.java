package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.item.ItemStockDecreaseService;
import com.hhplus.commerce.application.order.dto.OrderRequest;
import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {
    private final ItemStockDecreaseService itemStockDecreaseService;
    private final OrderCreateService orderCreateService;

    @Transactional
    public Order order(OrderRequest request) {
        // 재고 차감
        request.getOrderItemRequestList().forEach(orderItemRequest -> {
            OrderRequest.OrderItemOptionRequest orderItemOptionRequest
                    = orderItemRequest.getOrderItemOptionRequest();

            itemStockDecreaseService.decreaseStock(
                    orderItemOptionRequest.getItemOptionId(),
                    Long.valueOf(orderItemRequest.getOrderCount())
            );
        });

        //주문 저장
        return orderCreateService.createOrder(request);
    }
}
