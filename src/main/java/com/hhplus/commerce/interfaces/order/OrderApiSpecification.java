package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.OrderRequest;
import com.hhplus.commerce.domain.order.payment.PaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "order", description = "주문 API")
public interface OrderApiSpecification {
    @Operation(summary = "주문 생성", description = "💡주어진 식별자와 주문 정보로 해당하는 주문을 생성합니다.")
    CommonResponse createOrder(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "상품 정보") @RequestBody OrderRequest orderRequest
    );

    @Operation(summary = "주문 결제", description = "💡주어진 결제 정보로 결제합니다.")
    CommonResponse payOrder(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "결제 정보") @RequestBody PaymentRequest request
    );
}
