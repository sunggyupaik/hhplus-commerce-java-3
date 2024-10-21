package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.application.order.dto.OrderRequest;
import com.hhplus.commerce.application.order.dto.OrderResponse;
import com.hhplus.commerce.common.response.CommonResponse;
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

    final class Fake implements OrderApiSpecification {

        @Override
        public CommonResponse createOrder(Long customerId, OrderRequest orderRequest) {
            OrderResponse orderResponse = OrderResponse.builder()
                    .orderId(10L)
                    .build();

            return CommonResponse.success(orderResponse);
        }
    }
}
