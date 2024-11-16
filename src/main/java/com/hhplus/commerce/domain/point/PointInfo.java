package com.hhplus.commerce.domain.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointInfo {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long customerId;
        private Long point;

        public static DetailResponse of(Point point) {
            return DetailResponse.builder()
                    .customerId(point.getCustomerId())
                    .point(point.getPoint())
                    .build();
        }
    }
}
