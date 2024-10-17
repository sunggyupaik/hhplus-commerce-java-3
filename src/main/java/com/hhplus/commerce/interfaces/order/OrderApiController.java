package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.application.order.dto.PaymentResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.dto.OrderRequest;
import com.hhplus.commerce.domain.order.dto.OrderResponse;
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

    @PostMapping("/payment")
    public CommonResponse payOrder(
            @RequestHeader("customerId") Long customerId,
            @RequestBody PaymentRequest request
    ) {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(1L)
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .build();

        return CommonResponse.success(paymentResponse);
    }
}
