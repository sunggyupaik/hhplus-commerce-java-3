package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.dto.OrderRequest;
import com.hhplus.commerce.application.order.dto.OrderResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
public class OrderApiController implements OrderApiSpecification {
    @PostMapping
    public CommonResponse createOrder(
            @RequestHeader("customerId") Long customerId,
            @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(10L)
                .build();

        return CommonResponse.success(orderResponse);
    }
}
