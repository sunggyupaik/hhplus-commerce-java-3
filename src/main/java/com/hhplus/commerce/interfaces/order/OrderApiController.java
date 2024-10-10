package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.OrderRequest;
import com.hhplus.commerce.domain.order.OrderResponse;
import com.hhplus.commerce.domain.order.payment.PaymentRequest;
import com.hhplus.commerce.domain.order.payment.PaymentResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
public class OrderApiController implements OrderApiSpecification {
    @PostMapping("")
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
