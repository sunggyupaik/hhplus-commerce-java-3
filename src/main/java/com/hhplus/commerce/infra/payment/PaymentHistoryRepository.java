package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.domain.payment.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}
