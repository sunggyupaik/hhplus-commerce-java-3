package com.hhplus.commerce.infra.order.payment;

import com.hhplus.commerce.domain.order.payment.OrderPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<OrderPaymentHistory, Long> {
}
