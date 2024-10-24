package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointReader;
import com.hhplus.commerce.domain.point.history.PointHistory;
import com.hhplus.commerce.domain.point.history.PointHistoryStore;
import com.hhplus.commerce.domain.point.history.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointUseService {
    private final PointReader pointReader;
    private final PointHistoryStore pointHistoryStore;

    @Transactional
    public Long usePointWithPessimisticLock(Long customerId, PointRequest pointRequest) {
        Point point = pointReader.getPointWithPessimisticLock(customerId);
        Long leftPoint = point.use(pointRequest.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(customerId)
                .amount(pointRequest.getAmount())
                .type(PointType.USE)
                .build();

        pointHistoryStore.save(pointHistory);

        return leftPoint;
    }

    @Transactional
    public Long usePoint(Long customerId, PointRequest pointRequest) {
        Point point = pointReader.getPoint(customerId);
        Long leftPoint = point.use(pointRequest.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(customerId)
                .amount(pointRequest.getAmount())
                .type(PointType.USE)
                .build();

        pointHistoryStore.save(pointHistory);

        return leftPoint;
    }
}
