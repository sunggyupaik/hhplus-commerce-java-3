package com.hhplus.commerce.domain.order.payment;

import com.hhplus.commerce.common.BaseTimeEntity;
import com.hhplus.commerce.domain.order.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Order order;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Long amount;

    @Builder
    public Payment(
            Long id,
            Order order,
            Long customerId,
            PaymentMethod paymentMethod,
            Long amount
    ) {
        this.id = id;
        this.order = order;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }
}
