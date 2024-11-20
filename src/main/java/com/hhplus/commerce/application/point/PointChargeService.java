package com.hhplus.commerce.application.point;

import com.hhplus.commerce.support.distributedLock.DistributedLock;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointCommand;
import com.hhplus.commerce.domain.point.PointReader;
import com.hhplus.commerce.domain.point.history.PointHistory;
import com.hhplus.commerce.domain.point.history.PointHistoryStore;
import com.hhplus.commerce.domain.point.history.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointChargeService {
    private final PointReader pointReader;
    private final PointHistoryStore pointHistoryStore;

    /**
     * 비관 락으로 고객 포인트를 충전합니다
     * @param request 충전 요청 정보
     * @return 충전된 금액
     */
    @Transactional
    public Long chargePointWithPessimisticLock(PointCommand.ChargeRequest request) {
        Point point = pointReader.getPointWithPessimisticLock(request.getCustomerId());
        Long chargedPoint = point.charge(request.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .type(PointType.CHARGE)
                .build();

        pointHistoryStore.save(pointHistory);

        return chargedPoint;
    }

    /**
     * 낙관 락으로 고객 포인트를 충전합니다
     * @param request 충전 요청 정보
     * @return 충전된 금액
     */
    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public Long chargePointWithOptimisticLock(PointCommand.ChargeRequest request) {
        Point point = pointReader.getPointWithOptimisticLock(request.getCustomerId());
        Long chargedPoint = point.charge(request.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .type(PointType.CHARGE)
                .build();

        pointHistoryStore.save(pointHistory);

        return chargedPoint;
    }

    /**
     * 분산 락으로 고객 포인트를 충전합니다
     * @param request 충전 요청 정보
     * @return 충전된 금액
     */
    @DistributedLock(key = "'point'.concat(':').concat(#customerId)")
    public Long chargePointWithDistributedLock(PointCommand.ChargeRequest request) {
        Point point = pointReader.getPoint(request.getCustomerId());
        Long chargedPoint = point.charge(request.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .type(PointType.CHARGE)
                .build();

        pointHistoryStore.save(pointHistory);

        return chargedPoint;
    }
}
