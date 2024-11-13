package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.OrderFacade;
import com.hhplus.commerce.application.order.dto.OrderRequest;
import com.hhplus.commerce.application.order.dto.OrderResponse;
import com.hhplus.commerce.application.order.dto.OrderResultResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderApiController implements OrderApiSpecification {
    private final OrderFacade orderFacade;

    @PostMapping
    public CommonResponse createOrder(
            @RequestHeader("customerId") Long customerId,
            @RequestBody OrderRequest orderRequest
    ) {
        Order createdOrder = orderFacade.orderPessimisticLock(customerId, orderRequest);

        return CommonResponse.success(OrderResponse.of(createdOrder));
    }

    @GetMapping
    public CommonResponse getOrder(
            @RequestHeader("customerId") Long customerId
    ) {
        List<OrderResultResponse.OrderDetailResponse> orders = orderFacade.getOrders(customerId);
        return CommonResponse.success(OrderResultResponse.of(orders));
    }
}
