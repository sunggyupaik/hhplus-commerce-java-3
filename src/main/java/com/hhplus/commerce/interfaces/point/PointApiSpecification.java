package com.hhplus.commerce.interfaces.point;

import com.hhplus.commerce.application.point.dto.PointResponse;
import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.application.point.dto.PointChargeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "point", description = "포인트 API")
public interface PointApiSpecification {
    @Operation(summary = "포인트 잔액 조회", description = "💡주어진 식별자에 해당하는 고객의 포인트 잔액을 반환합니다.")
    CommonResponse getPoint(
            @Parameter(description = "고객 식별자") Long customerId
    );

    @Operation(summary = "포인트 충전", description = "💡주어진 식별자와 금액으로 해당 고객의 포인트를 충전하고 반환합니다")
    CommonResponse chargePoint(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "포인트 충전 정보") PointChargeRequest request
    );

    final class Fake implements PointApiSpecification {
        @Override
        public CommonResponse getPoint(Long customerId) {
            PointResponse pointResponse = PointResponse.builder()
                    .customerId(customerId)
                    .point(1000L)
                    .build();

            return CommonResponse.success(pointResponse);
        }

        @Override
        public CommonResponse chargePoint(Long customerId, PointChargeRequest request) {
            PointResponse pointResponse = PointResponse.builder()
                    .customerId(customerId)
                    .point(2000L)
                    .build();

            return CommonResponse.success(pointResponse);
        }
    }
}
