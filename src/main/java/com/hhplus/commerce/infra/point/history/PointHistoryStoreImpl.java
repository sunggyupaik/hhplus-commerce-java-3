package com.hhplus.commerce.infra.point.history;

import com.hhplus.commerce.domain.point.history.PointHistory;
import com.hhplus.commerce.domain.point.history.PointHistoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointHistoryStoreImpl implements PointHistoryStore {
    private final PointHistoryRepository pointHistoryRepository;

    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryRepository.save(pointHistory);
    }
}
