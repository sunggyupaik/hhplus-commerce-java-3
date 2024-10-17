package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointResponse;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointQueryService {
    private final PointReader pointReader;

    public PointResponse getPoint(Long customerId) {
        Point point = pointReader.getPoint(customerId);

        return PointResponse.of(point);
    }
}
