package com.hhplus.commerce.interfaces.point;

import com.hhplus.commerce.domain.point.PointInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "잔액 충전 요청")
    public static class ChargeRequest {
        @Schema(description = "충전 할 포인트", example = "1000")
        private Long amount;

        public static ChargeRequest of(Long amount) {
            return ChargeRequest.builder()
                    .amount(amount)
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(description = "잔액 충전 응답")
    public static class DetailResponse {
        @Schema(description = "고객 식별자", example = "1")
        private Long customerId;

        @Schema(description = "충전 후 포인트", example = "1000")
        private Long point;

        public static DetailResponse of(PointInfo.DetailResponse response) {
            return DetailResponse.builder()
                    .customerId(response.getCustomerId())
                    .point(response.getPoint())
                    .build();
        }
    }
}
