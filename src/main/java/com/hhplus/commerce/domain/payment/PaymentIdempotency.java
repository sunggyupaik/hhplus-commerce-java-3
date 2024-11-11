package com.hhplus.commerce.domain.payment;

import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    public static PaymentIdempotency of(PaymentRequest paymentRequest) {
        return PaymentIdempotency.builder()
                .idempotencyKey(paymentRequest.getIdempotencyKey())
                .orderId(paymentRequest.getOrderId())
                .build();
    }

    public boolean isIdempotencyKeySame(String idempotencyKey) {
        return this.idempotencyKey.equals(idempotencyKey);
    }
}
