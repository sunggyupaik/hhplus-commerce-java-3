package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.OrderFacade;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.OrderCommand;
import com.hhplus.commerce.domain.order.OrderInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
public class OrderApiStrategyController {
    private final OrderFacade orderFacade;

    @PostMapping("/p")
    public CommonResponse createOrderPessimistic(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid OrderDto.OrderRequest orderRequest
    ) {
        var command = OrderCommand.OrderRequest.of(customerId, orderRequest);
        OrderInfo.CreateResponse response = orderFacade.orderPessimisticLock(command);

        return CommonResponse.success(OrderDto.OrderResponse.of(response));
    }

    @PostMapping("/o")
    public CommonResponse createOrderOptimistic(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid OrderDto.OrderRequest orderRequest
    ) {
        var command = OrderCommand.OrderRequest.of(customerId, orderRequest);
        OrderInfo.CreateResponse response = orderFacade.orderOptimisticLock(command);

        return CommonResponse.success(OrderDto.OrderResponse.of(response));
    }

    @PostMapping("/d")
    public CommonResponse createOrderDistributed(
            @RequestHeader("customerId") Long customerId,
            @RequestBody @Valid OrderDto.OrderRequest orderRequest
    ) {
        var command = OrderCommand.OrderRequest.of(customerId, orderRequest);
        OrderInfo.CreateResponse response = orderFacade.orderDistributedLock(command);

        return CommonResponse.success(OrderDto.OrderResponse.of(response));
    }
}
