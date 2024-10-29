package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.status = \"INIT\"")
    List<Order> getInitOrders();
}