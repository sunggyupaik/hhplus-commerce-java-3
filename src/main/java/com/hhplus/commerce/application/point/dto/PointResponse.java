package com.hhplus.commerce.application.point.dto;

import com.hhplus.commerce.domain.point.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "잔액 충전 응답")
public class PointResponse {
    @Schema(description = "고객 식별자", example = "1")
    private Long customerId;

    @Schema(description = "충전 후 포인트", example = "1000")
    private Long point;

    public static PointResponse of(Point point) {
        return PointResponse.builder()
                .customerId(point.getCustomerId())
                .point(point.getPoint())
                .build();
    }
}
