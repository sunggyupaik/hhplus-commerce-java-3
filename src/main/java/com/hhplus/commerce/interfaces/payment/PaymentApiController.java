package com.hhplus.commerce.interfaces.payment;

import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.application.payment.dto.PaymentResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentApiController {
    @PostMapping
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
