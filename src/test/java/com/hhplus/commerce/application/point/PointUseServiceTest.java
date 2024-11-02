package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointReader;
import com.hhplus.commerce.domain.point.history.PointHistory;
import com.hhplus.commerce.domain.point.history.PointHistoryStore;
import com.hhplus.commerce.domain.point.history.PointType;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"InnerClassMayBeStatic"})
@DisplayName("PointUseService 클래스")
class PointUseServiceTest {
    private PointUseService pointUseService;
    private PointReader pointReader;
    private PointHistoryStore pointHistoryStore;

    @BeforeEach
    void setUp() {
        pointReader = mock(PointReader.class);
        pointHistoryStore = mock(PointHistoryStore.class);
        pointUseService = new PointUseService(pointReader, pointHistoryStore);
    }

    @Nested
    @DisplayName("usePoint 메소드는")
    class Describe_usePoint {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자와 금액이 주어진다면")
        class Context_with_existed_customer_id_and_amount {
            private final Long CUSTOMER_ID_1L = 1L;
            private final Long POINT_5000L = 5000L;
            private final Long USE_POINT_3000L = 3000L;

            @Test
            @DisplayName("포인트를 계산하고 충전 이력을 저장하고 잔액을 반환한다")
            void it_returns_left_point() {
                Point point = createPoint(CUSTOMER_ID_1L, POINT_5000L);
                PointHistory pointHistory = createPointHistory(CUSTOMER_ID_1L, USE_POINT_3000L, PointType.USE);
                PointRequest request = createPointChargeRequest(USE_POINT_3000L);
                given(pointReader.getPointWithPessimisticLock(CUSTOMER_ID_1L)).willReturn(point);
                given(pointHistoryStore.save(pointHistory)).willReturn(pointHistory);

                Long leftPoint = pointUseService.usePointWithPessimisticLock(CUSTOMER_ID_1L, request);

                Assertions.assertEquals(leftPoint, POINT_5000L - request.getAmount(),
                        "반환된 금액은 기존에 보유한 금액에서 요청한 금액을 뺀 값이다.");
                Assertions.assertEquals(pointHistory.getType(), PointType.USE,
                        "포인트 이력의 타입은 사용이다");
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 고객 식별자가 주어진다면")
        class Context_with_not_existed_customer_id {
            private final Long notExistedCustomerId = 99L;

            @Test
            @DisplayName("포인트가 존재하지 않다는 예외를 반환한다")
            void it_throws_point_not_exists() {
                PointRequest request = createPointChargeRequest(5000L);
                given(pointReader.getPointWithPessimisticLock(notExistedCustomerId))
                        .willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> pointUseService.usePointWithPessimisticLock(notExistedCustomerId, request)
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
                PointRequest request = createPointChargeRequest(5000000L);
                given(pointReader.getPointWithPessimisticLock(existedCustomerId)).willReturn(point);

                assertThatThrownBy(
                        () -> pointUseService.usePointWithPessimisticLock(existedCustomerId, request)
                )
                        .isInstanceOf(IllegalStatusException.class);
            }
        }
    }

    private PointHistory createPointHistory(Long customerId, Long amount, PointType type) {
        return PointHistory.builder()
                .customerId(customerId)
                .amount(amount)
                .type(type)
                .build();
    }

    private Point createPoint(Long customerId, Long point) {
        return Point.builder()
                .customerId(customerId)
                .point(point)
                .build();
    }

    private PointRequest createPointChargeRequest(Long amount) {
        return PointRequest.builder()
                .amount(amount)
                .build();
    }
}
