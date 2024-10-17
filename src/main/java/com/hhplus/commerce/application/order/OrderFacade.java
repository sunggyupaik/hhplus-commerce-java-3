package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.item.ItemStockDecreaseService;
import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.application.point.PointUseService;
import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {
    private final ItemStockDecreaseService itemStockDecreaseService;
    private final OrderCreateService orderCreateService;
    private final PointUseService pointUseService;
    private final PaymentCreateService paymentCreateService;

    @Transactional
    public Order order(OrderRequest request) {
        // 재고차감
        request.getOrderItemRequestList().forEach(orderItemRequest -> {
            OrderRequest.OrderItemOptionRequest orderItemOptionRequest = orderItemRequest.getOrderItemOptionRequest();
            itemStockDecreaseService.decreaseStock(orderItemOptionRequest.getItemOptionId(), Long.valueOf(orderItemRequest.getOrderCount()));
        });

        //주문 저장
        return orderCreateService.createOrder(request);
    }

    @Transactional
    public Long pay(Order order, PaymentRequest paymentRequest) {
        //포인트차감
        PointRequest pointRequest = PointRequest.builder().amount(paymentRequest.getAmount()).build();
        Long leftPoint = pointUseService.usePoint(paymentRequest.getCustomerId(), pointRequest);

        //결제
        paymentCreateService.createPayment(order, paymentRequest);

        return leftPoint;
    }
}
