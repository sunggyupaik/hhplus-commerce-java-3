package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointResponse;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
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
@DisplayName("PointQueryService 클래스")
class PointQueryServiceTest {
    private PointQueryService pointQueryService;
    private PointReader pointReader;

    @BeforeEach
    void setUp() {
        pointReader = mock(PointReader.class);
        pointQueryService = new PointQueryService(pointReader);
    }

    @Nested
    @DisplayName("getPoint 메소드는")
    class Describe_getPoint {
        @Nested
        @DisplayName("만약 존재하는 고객 식별자가 주어진다면")
        class Context_with_existed_customer_id {
            private final Long existedCustomerId = 1L;

            @Test
            @DisplayName("포인트 정보를 반환한다")
            void it_returns_point() {
                Point point = createPoint(existedCustomerId, 1000L);

                given(pointReader.getPoint(existedCustomerId)).willReturn(point);

                PointResponse pointResponse = pointQueryService.getPoint(existedCustomerId);

                assertThat(pointResponse.getCustomerId()).isEqualTo(existedCustomerId);
                assertThat(pointResponse.getPoint()).isEqualTo(1000L);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 고객 식별자가 주어진다면")
        class Context_with_not_existed_customer_id {
            private final Long notExistedCustomerId = 99L;

            @Test
            @DisplayName("포인트가 존재하지 않다는 예외를 반환한다")
            void it_throws_point_not_exists() {
                given(pointReader.getPoint(notExistedCustomerId)).willThrow(EntityNotFoundException.class);

                assertThatThrownBy(
                        () -> pointQueryService.getPoint(notExistedCustomerId)
                )
                        .isInstanceOf(EntityNotFoundException.class);
            }
        }
    }

    private Point createPoint(Long customerId, Long point) {
        return Point.builder()
                .customerId(customerId)
                .point(point)
                .build();
    }
}