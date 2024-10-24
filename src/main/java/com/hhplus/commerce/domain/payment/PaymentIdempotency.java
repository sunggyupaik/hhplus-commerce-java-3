package com.hhplus.commerce.domain.payment;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_idempotency")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class PaymentIdempotency extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String idempotencyKey;

    @Builder
    public PaymentIdempotency(
            Long id,
            Long orderId,
            String idempotencyKey
    ) {
        this.id = id;
        this.orderId = orderId;
        this.idempotencyKey = idempotencyKey;
    }

    public void changeIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public boolean isIdempotencyKeySame(String idempotencyKey) {
        return this.idempotencyKey.equals(idempotencyKey);
    }
}
