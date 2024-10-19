package com.hhplus.commerce.domain.order.payment;

import com.hhplus.commerce.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_payment_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderPaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private Order order;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Long amount;

    private String code;

    private String message;

    @Builder
    public OrderPaymentHistory(
            Long id,
            Order order,
            Long customerId,
            PaymentMethod paymentMethod,
            Long amount,
            String code,
            String message
    ) {
        this.id = id;
        this.order = order;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.code = code;
        this.message = message;
    }
}
