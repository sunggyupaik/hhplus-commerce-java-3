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
@DisplayName("PointChargeService 클래스")
class PointChargeServiceTest {
    private PointChargeService pointChargeService;
    private PointReader pointReader;
    private PointHistoryStore pointHistoryStore;

    @BeforeEach
    void setUp() {
        pointReader = mock(PointReader.class);
        pointHistoryStore = mock(PointHistoryStore.class);
        pointChargeService = new PointChargeService(pointReader, pointHistoryStore);
    }

    @Nested
    @DisplayName("chargePoint 메소드는")
    class Describe_chargePoint {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자와 금액이 주어진다면")
        class Context_with_existed_customer_id_and_amount {
            private final Long EXISTED_CUSTOMER_ID = 1L;
            private final Long AMOUNT_1000 = 1000L;
            private final Long CHARGE_AMOUNT_5000 = 5000L;

            @Test
            @DisplayName("포인트를 충전하고 충전 이력을 저장하고 충전된 금액을 반환한다")
            void it_returns_charged_point() {
                Point point = createPoint(EXISTED_CUSTOMER_ID, AMOUNT_1000);
                PointHistory pointHistory = createPointHistory(EXISTED_CUSTOMER_ID, CHARGE_AMOUNT_5000, PointType.CHARGE);
                PointRequest request = createPointRequest(CHARGE_AMOUNT_5000);
                given(pointReader.getPointWithPessimisticLock(EXISTED_CUSTOMER_ID)).willReturn(point);
                given(pointHistoryStore.save(pointHistory)).willReturn(pointHistory);

                Long chargedPoint = pointChargeService.chargePoint(EXISTED_CUSTOMER_ID, request);

                Assertions.assertEquals(chargedPoint, AMOUNT_1000 + request.getAmount(),
                        "반환된 금액은 보유한 금액과 요청한 금액의 합이다");
                Assertions.assertEquals(pointHistory.getType(), PointType.CHARGE,
                        "포인트의 타입은 충전이다");

            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 고객 식별자가 주어진다면")
        class Context_with_not_existed_customer_id {
            private final Long notExistedCustomerId = 99L;

            @Test
            @DisplayName("포인트가 존재하지 않다는 예외를 반환한다")
            void it_throws_point_not_exists() {
                PointRequest request = createPointRequest(5000L);
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
            void it_throws_point_overs_max() {
                Point point = createPoint(existedCustomerId, 1000L);
                PointRequest request = createPointRequest(5000000L);
                given(pointReader.getPointWithPessimisticLock(existedCustomerId)).willReturn(point);

                assertThatThrownBy(
                        () -> pointChargeService.chargePoint(existedCustomerId, request)
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

    private PointRequest createPointRequest(Long amount) {
        return PointRequest.builder()
                .amount(amount)
                .build();
    }
}