package com.hhplus.commerce.domain.point;

public interface PointReader {
    Point getPoint(Long customerId);

    Point getPointWithPessimisticLock(Long customerId);
}
