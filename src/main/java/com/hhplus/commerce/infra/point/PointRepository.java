package com.hhplus.commerce.infra.point;

import com.hhplus.commerce.domain.point.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Point p where p.customerId = :customerId")
    Optional<Point> findByIdWithPessimisticLock(@Param("customerId") Long customerId);
}
