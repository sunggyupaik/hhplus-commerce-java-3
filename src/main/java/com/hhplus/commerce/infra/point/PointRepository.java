package com.hhplus.commerce.infra.point;

import com.hhplus.commerce.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
