package com.hhplus.commerce.interfaces.payment;

import com.hhplus.commerce.application.payment.PaymentFacade;
import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.application.payment.dto.PaymentResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class PaymentApiController {
    private final PaymentFacade paymentFacade;

    @PostMapping
    public CommonResponse payOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestHeader("customerId") Long customerId,
            @RequestBody PaymentRequest request
    ) {
        request.addCustomerId(customerId);
        request.addIdempotencyKey(idempotencyKey);
        PaymentResponse paymentResponse = paymentFacade.payOrder(request);

        return CommonResponse.success(paymentResponse);
    }
}
