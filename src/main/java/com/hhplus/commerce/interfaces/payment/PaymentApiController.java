package com.hhplus.commerce.interfaces.payment;

import com.hhplus.commerce.application.payment.PaymentFacade;
import com.hhplus.commerce.support.response.CommonResponse;
import com.hhplus.commerce.domain.payment.PaymentCommand;
import com.hhplus.commerce.domain.payment.PaymentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
public class PaymentApiController {
    private final PaymentFacade paymentFacade;

    @PostMapping
    public CommonResponse payOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestHeader("customerId") Long customerId,
            @RequestBody PaymentDto.PaymentRequest request
    ) {
        var command = PaymentCommand.PayOrderRequest.of(request, customerId, idempotencyKey);
        PaymentInfo.PayOrderResponse response = paymentFacade.payOrder(command);

        return CommonResponse.success(PaymentDto.PayOrderResponse.of(response));
    }
}
