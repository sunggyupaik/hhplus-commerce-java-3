package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointChargeRequest;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointUseService {
    private final PointReader pointReader;

    @Transactional
    public Long usePoint(Long customerId, PointChargeRequest pointChargeRequest) {
        Point point = pointReader.getPointWithPessimisticLock(customerId);
        return point.use(pointChargeRequest.getAmount());
    }
}
