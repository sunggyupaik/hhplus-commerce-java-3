package com.hhplus.commerce.domain.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "잔액 충전 요청")
public class PointChargeRequest {
    @Schema(description = "충전 할 포인트", example = "1000")
    private Long amount;
}
