package com.hhplus.commerce.application.point;

import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointCommand;
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
    public Long usePointWithPessimisticLock(PointCommand.ChargeRequest request) {
        Point point = pointReader.getPointWithPessimisticLock(request.getCustomerId());
        Long leftPoint = point.use(request.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .type(PointType.USE)
                .build();

        pointHistoryStore.save(pointHistory);

        return leftPoint;
    }

    @Transactional
    public Long usePoint(PointCommand.ChargeRequest request) {
        Point point = pointReader.getPoint(request.getCustomerId());
        Long leftPoint = point.use(request.getAmount());

        PointHistory pointHistory = PointHistory.builder()
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .type(PointType.USE)
                .build();

        pointHistoryStore.save(pointHistory);

        return leftPoint;
    }
}
