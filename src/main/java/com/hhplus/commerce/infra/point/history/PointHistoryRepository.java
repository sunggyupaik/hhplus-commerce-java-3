package com.hhplus.commerce.infra.point.history;

import com.hhplus.commerce.domain.point.history.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
