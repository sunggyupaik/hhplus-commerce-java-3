package com.hhplus.commerce.domain.point;

import com.hhplus.commerce.interfaces.point.PointDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointCommand {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChargeRequest {
        private Long customerId;
        private Long amount;

        public static ChargeRequest of(Long customerId, PointDto.ChargeRequest request) {
            return ChargeRequest.builder()
                    .customerId(customerId)
                    .amount(request.getAmount())
                    .build();
        }

        public static ChargeRequest of(Long customerId, Long amount) {
            return ChargeRequest.builder()
                    .customerId(customerId)
                    .amount(amount)
                    .build();
        }
    }
}
