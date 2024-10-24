package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.domain.payment.PaymentIdempotency;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentIdempotencyRepository extends JpaRepository<PaymentIdempotency, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select pi from PaymentIdempotency pi where pi.orderId = :orderId and pi.idempotencyKey = :idempotencyKey")
    //@QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    Optional<PaymentIdempotency> findByOrderIdWithPessimisticLock(
            @Param("orderId") Long orderId,
            @Param("idempotencyKey") String idempotencyKey
    );

    Optional<PaymentIdempotency> findByOrderIdAndIdempotencyKey(Long orderId, String idempotencyKey);

    @Query("select COUNT(pi.id) > 0 from PaymentIdempotency pi where pi.orderId = :orderId and pi.idempotencyKey = :idempotencyKey")
    boolean existsByOrderIdAndIdempotencyKey(
            @Param("orderId") Long orderId,
            @Param("idempotencyKey") String idempotencyKey
    );
}
