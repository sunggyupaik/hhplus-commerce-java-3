package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.OrderFacade;
import com.hhplus.commerce.support.response.CommonResponse;
import com.hhplus.commerce.domain.order.OrderCommand;
import com.hhplus.commerce.domain.order.OrderInfo;
import jakarta.validation.Valid;
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
            @RequestBody @Valid OrderDto.OrderRequest orderRequest
    ) {
        var command = OrderCommand.OrderRequest.of(customerId, orderRequest);
        OrderInfo.CreateResponse response = orderFacade.orderPessimisticLock(command);

        return CommonResponse.success(OrderDto.OrderResponse.of(response));
    }

    @GetMapping
    public CommonResponse getOrders(
            @RequestHeader("customerId") Long customerId
    ) {
        List<OrderInfo.DetailResponse> ordersInfo = orderFacade.getOrders(customerId);
        return CommonResponse.success(OrderDto.ResultResponse.of(ordersInfo));
    }
}
