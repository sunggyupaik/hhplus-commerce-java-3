package com.hhplus.commerce.domain.payment;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Long amount;

    @Builder
    public Payment(
            Long id,
            Long orderId,
            Long customerId,
            PaymentMethod paymentMethod,
            Long amount
    ) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public static Payment empty() {
        return Payment.builder().build();
    }
}
