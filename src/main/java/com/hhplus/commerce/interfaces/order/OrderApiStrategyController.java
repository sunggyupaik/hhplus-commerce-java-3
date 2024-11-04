package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.OrderFacade;
import com.hhplus.commerce.application.order.dto.OrderRequest;
import com.hhplus.commerce.application.order.dto.OrderResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
public class OrderApiStrategyController {
    private final OrderFacade orderFacade;

    @PostMapping("/p")
    public CommonResponse createOrderPessimistic(
            @RequestHeader("customerId") Long customerId,
            @RequestBody OrderRequest orderRequest
    ) {
        Order createdOrder = orderFacade.orderPessimisticLock(customerId, orderRequest);

        return CommonResponse.success(OrderResponse.of(createdOrder));
    }

    @PostMapping("/o")
    public CommonResponse createOrderOptimistic(
            @RequestHeader("customerId") Long customerId,
            @RequestBody OrderRequest orderRequest
    ) {
        Order createdOrder = orderFacade.orderOptimisticLock(customerId, orderRequest);

        return CommonResponse.success(OrderResponse.of(createdOrder));
    }

    @PostMapping("/d")
    public CommonResponse createOrderDistributed(
            @RequestHeader("customerId") Long customerId,
            @RequestBody OrderRequest orderRequest
    ) {
        Order createdOrder = orderFacade.orderDistributedLock(customerId, orderRequest);

        return CommonResponse.success(OrderResponse.of(createdOrder));
    }
}
