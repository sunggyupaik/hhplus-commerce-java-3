package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.application.order.dto.PaymentResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.dto.OrderRequest;
import com.hhplus.commerce.domain.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "order", description = "주문 API")
public interface OrderApiSpecification {
    @Operation(summary = "주문 생성", description = "💡주어진 식별자와 주문 정보로 해당하는 주문을 생성합니다.")
    CommonResponse createOrder(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "상품 정보") OrderRequest orderRequest
    );

    @Operation(summary = "주문 결제", description = "💡주어진 결제 정보로 결제합니다.")
    CommonResponse payOrder(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "결제 정보") PaymentRequest request
    );

    final class Fake implements OrderApiSpecification {

        @Override
        public CommonResponse createOrder(Long customerId, OrderRequest orderRequest) {
            OrderResponse orderResponse = OrderResponse.builder()
                    .orderId(10L)
                    .build();

            return CommonResponse.success(orderResponse);
        }

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
