package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.domain.order.item.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi.itemId, sum(oi.orderCount) AS sum " +
            "FROM OrderItem oi " +
            "WHERE oi.createdDate " +
            "BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.itemId " +
            "ORDER BY sum " +
            "DESC LIMIT 5")
    List<ItemBestResponse> findTop5ByOrderCountSum(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
}
