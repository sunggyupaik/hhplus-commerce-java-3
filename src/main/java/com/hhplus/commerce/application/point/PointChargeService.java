package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.common.distributedLock.DistributedLock;
import com.hhplus.commerce.domain.point.Point;
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
     * @param customerId 고객 식별자
     * @param pointRequest 포인트
     * @return 충전된 금액
     */
    @Transactional
    public Long chargePointWithPessimisticLock(Long customerId, PointRequest pointRequest) {
        Point point = pointReader.getPointWithPessimisticLock(customerId);
        Long chargedPoint = point.charge(pointRequest.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(customerId)
                .amount(pointRequest.getAmount())
                .type(PointType.CHARGE)
                .build();

        pointHistoryStore.save(pointHistory);

        return chargedPoint;
    }

    /**
     * 낙관 락으로 고객 포인트를 충전합니다
     * @param customerId 고객 식별자
     * @param pointRequest 포인트
     * @return 충전된 금액
     */
    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public Long chargePointWithOptimisticLock(Long customerId, PointRequest pointRequest) {
        Point point = pointReader.getPointWithOptimisticLock(customerId);
        Long chargedPoint = point.charge(pointRequest.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(customerId)
                .amount(pointRequest.getAmount())
                .type(PointType.CHARGE)
                .build();

        pointHistoryStore.save(pointHistory);

        return chargedPoint;
    }

    /**
     * 분산 락으로 고객 포인트를 충전합니다
     * @param customerId 고객 식별자
     * @param pointRequest 포인트
     * @return 충전된 금액
     */
    @DistributedLock(key = "'point'.concat(':').concat(#customerId)")
    public Long chargePointWithDistributedLock(Long customerId, PointRequest pointRequest) {
        Point point = pointReader.getPoint(customerId);
        Long chargedPoint = point.charge(pointRequest.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(customerId)
                .amount(pointRequest.getAmount())
                .type(PointType.CHARGE)
                .build();

        pointHistoryStore.save(pointHistory);

        return chargedPoint;
    }
}
