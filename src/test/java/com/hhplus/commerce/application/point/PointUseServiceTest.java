package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointChargeRequest;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("PointUseService 클래스")
class PointUseServiceTest {
    private PointUseService pointUseService;
    private PointReader pointReader;

    @BeforeEach
    void setUp() {
        pointReader = mock(PointReader.class);
        pointUseService = new PointUseService(pointReader);
    }

    @Nested
    @DisplayName("usePoint 메소드는")
    class Describe_usePoint {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자와 금액이 주어진다면")
        class Context_with_existed_customer_id_and_amount {
            private final Long existedCustomerId = 1L;

            @Test
            @DisplayName("포인트를 사용하고 잔액을 반환한다")
            void it_uses_point_and_returns_left_point() {
                Point point = createPoint(existedCustomerId, 5000L);
                PointChargeRequest request = createPointChargeRequest(3000L);
                given(pointReader.getPointWithPessimisticLock(existedCustomerId)).willReturn(point);

                Long leftPoint = pointUseService.usePoint(existedCustomerId, request);

                assertThat(leftPoint).isEqualTo(5000L - request.getAmount());
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 고객 식별자가 주어진다면")
        class Context_with_not_existed_customer_id {
            private final Long notExistedCustomerId = 99L;

            @Test
            @DisplayName("포인트가 존재하지 않다는 예외를 반환한다")
            void it_throws_point_not_exists() {
                PointChargeRequest request = createPointChargeRequest(5000L);
                given(pointReader.getPointWithPessimisticLock(notExistedCustomerId)).willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> pointUseService.usePoint(notExistedCustomerId, request)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 0원 미만을 만드는 금액이 주어진다면")
        class Context_with_balance_less_than_zero {
            private final Long existedCustomerId = 1L;

            @Test
            @DisplayName("포인트가 최대를 초과했다는 예외를 반환한다.")
            void it_throws_point_insufficient() {
                Point point = createPoint(existedCustomerId, 1000L);
                PointChargeRequest request = createPointChargeRequest(5000000L);
                given(pointReader.getPointWithPessimisticLock(existedCustomerId)).willReturn(point);

                assertThatThrownBy(
                        () -> pointUseService.usePoint(existedCustomerId, request)
                )
                        .isInstanceOf(IllegalStatusException.class);
            }
        }
    }

    private Point createPoint(Long customerId, Long point) {
        return Point.builder()
                .customerId(customerId)
                .point(point)
                .build();
    }

    private PointChargeRequest createPointChargeRequest(Long amount) {
        return PointChargeRequest.builder()
                .amount(amount)
                .build();
    }
}
