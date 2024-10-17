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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("PointChargeService 클래스")
class PointChargeServiceTest {
    private PointChargeService pointChargeService;
    private PointReader pointReader;

    @BeforeEach
    void setUp() {
        pointReader = mock(PointReader.class);
        pointChargeService = new PointChargeService(pointReader);
    }

    @Nested
    @DisplayName("chargePoint 메소드는")
    class Describe_chargePoint {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자와 금액이 주어진다면")
        class Context_with_existed_customer_id_and_amount {
            private final Long existedCustomerId = 1L;

            @Test
            @DisplayName("포인트를 충전하고 충전된 금액을 반환한다")
            void it_charges_point_and_returns_charged_point() {
                Point point = createPoint(existedCustomerId, 1000L);
                PointChargeRequest request = createPointChargeRequest(5000L);
                given(pointReader.getPointWithPessimisticLock(existedCustomerId)).willReturn(point);

                Long chargedPoint = pointChargeService.chargePoint(existedCustomerId, request);

                assertThat(chargedPoint).isEqualTo(1000L + request.getAmount());
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
                        () -> pointChargeService.chargePoint(notExistedCustomerId, request)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 최대 한도를 초과하는 금액이 주어진다면")
        class Context_with_balance_over_amount {
            private final Long existedCustomerId = 1L;

            @Test
            @DisplayName("포인트가 최대를 초과했다는 예외를 반환한다.")
            void it_throws_point_not_exists() {
                Point point = createPoint(existedCustomerId, 1000L);
                PointChargeRequest request = createPointChargeRequest(5000000L);
                given(pointReader.getPointWithPessimisticLock(existedCustomerId)).willReturn(point);

                assertThatThrownBy(
                        () -> pointChargeService.chargePoint(existedCustomerId, request)
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