package com.hhplus.commerce.domain.payment;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class PaymentIdempotency {
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
}
