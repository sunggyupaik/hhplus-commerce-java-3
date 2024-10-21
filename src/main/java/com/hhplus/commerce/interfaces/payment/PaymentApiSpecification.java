package com.hhplus.commerce.interfaces.payment;

import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.application.payment.dto.PaymentResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "payment", description = "결제 API")
public interface PaymentApiSpecification {
    @Operation(summary = "주문 결제", description = "💡주어진 결제 정보로 결제합니다.")
    CommonResponse payOrder(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "결제 정보") PaymentRequest request
    );

    final class Fake implements PaymentApiSpecification {
        @Override
        public CommonResponse payOrder(Long customerId, PaymentRequest request) {
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .paymentId(1L)
                    .orderId(request.getOrderId())
                    .amount(request.getAmount())
                    .paymentMethod(request.getPaymentMethod())
                    .build();

            return CommonResponse.success(paymentResponse);
        }
    }
}
