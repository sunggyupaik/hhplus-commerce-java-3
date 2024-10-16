package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
