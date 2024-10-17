package com.hhplus.commerce.infra.order.payment;

import com.hhplus.commerce.domain.order.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
