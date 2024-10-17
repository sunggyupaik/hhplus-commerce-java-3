package com.hhplus.commerce.infra.point;

import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointReaderImpl implements PointReader {
    private final PointRepository pointRepository;

    @Override
    public Point getPoint(Long customerId) {
        return pointRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POINT_NOT_FOUND));
    }

    @Override
    public Point getPointWithPessimisticLock(Long customerId) {
        return pointRepository.findByIdWithPessimisticLock(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POINT_NOT_FOUND));
    }
}
