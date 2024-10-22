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
public class OrderApiController implements OrderApiSpecification {
    private final OrderFacade orderFacade;

    @PostMapping
    public CommonResponse createOrder(
            @RequestHeader("customerId") Long customerId,
            @RequestBody OrderRequest orderRequest
    ) {
        orderRequest.addCustomerId(customerId);
        Order createdOrder = orderFacade.order(orderRequest);

        return CommonResponse.success(OrderResponse.of(createdOrder));
    }
}
