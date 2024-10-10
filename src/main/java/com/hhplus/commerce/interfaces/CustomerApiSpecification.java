package com.hhplus.commerce.interfaces;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.customer.PointChargeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "customer", description = "고객 API")
public interface CustomerApiSpecification {
    @Operation(summary = "포인트 잔액 조회", description = "💡주어진 식별자에 해당하는 고객의 포인트 잔액을 반환합니다.")
    CommonResponse getCustomerPoint(
            @RequestHeader("Authorization") Long customerId
    );

    @Operation(summary = "포인트 잔액 조회", description = "💡주어진 식별자와 금액으로 해당 고객의 포인트를 충전하고 반환합니다")
    CommonResponse chargeCustomerPoint(
            @RequestHeader("Authorization") Long customerId,
            @RequestBody PointChargeRequest request
    );
}
