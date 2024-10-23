package com.hhplus.commerce.domain.point;

import com.hhplus.commerce.common.exception.IllegalStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PointTest {
    @Autowired private PointStore pointStore;

    @Test
    @DisplayName("주어진 금액으로 기존 금액을 충전한다")
    void testCharge() {
        final Long POINT_1000 = 1000L;
        final Long AMOUNT_2000 = 2000L;

        Point point = pointFixture(POINT_1000);

        point.charge(AMOUNT_2000);

        Assertions.assertEquals(POINT_1000 + AMOUNT_2000, point.getPoint(),
                "기존 1000원에서 2000원을 충전하면 3000원이다");
    }

    @Test
    @DisplayName("충전 이후 최대 금액을 넘어서면 최대 금액을 초과했다는 예외를 반환한다")
    void testChargeOverThanMaxPoint() {
        final Long POINT_1000 = 1000L;
        final Long AMOUNT_100000L = Point.MAX_POINT;

        Point point = pointFixture(POINT_1000);

        assertThatThrownBy(
                () -> point.charge(AMOUNT_100000L)
        )
                .isInstanceOf(IllegalStatusException.class);
    }

    @Test
    @DisplayName("주어진 금액으로 기존 금액에서 차감한다")
    void testUse() {
        final Long POINT_2000 = 2000L;
        final Long AMOUNT_1000 = 1000L;

        Point point = pointFixture(POINT_2000);

        point.use(AMOUNT_1000);

        Assertions.assertEquals(POINT_2000 - AMOUNT_1000, point.getPoint(),
                "기존 2000원에서 1000원을 차감하면 1000원이다");
    }

    @Test
    @DisplayName("차감 이후 0원 미만이면 잔액이 0원 미만이라는 예외를 반환한다")
    void testChargeLessThanMaxZero() {
        final Long POINT_1000 = 1000L;
        final Long AMOUNT_2000L = 2000L;

        Point point = pointFixture(POINT_1000);

        assertThatThrownBy(
                () -> point.use(AMOUNT_2000L)
        )
                .isInstanceOf(IllegalStatusException.class);
    }

    private Point pointFixture(Long pointAmount) {
        Point point = createPoint(pointAmount);

        return pointStore.save(point);
    }

    private Point createPoint(Long point) {
        return Point.builder()
                .point(point)
                .build();
    }
}
