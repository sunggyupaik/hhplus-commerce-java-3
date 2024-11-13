package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.domain.order.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.status = \"INIT\"")
    List<Order> getInitOrders();

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :id")
    Optional<Order> findByIdWithPessimisticLock(@Param("id") Long id);

    @Query("select o from Order o join fetch o.orderItems oi join fetch oi.orderItemOption oio")
    List<Order> findByCustomerId(Long customerId);
}