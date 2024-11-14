package com.hhplus.commerce.domain.payment;

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
@Table(name = "payment_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class PaymentHistory {
    private static final String SUCCESS = "SUCCESS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long customerId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Long amount;

    private String code;

    private String message;

    @Builder
    public PaymentHistory(
            Long id,
            Long orderId,
            Long customerId,
            PaymentMethod paymentMethod,
            Long amount,
            String code,
            String message
    ) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccessHistory() {
        return code.equals(SUCCESS) && message.equals(SUCCESS);
    }
}
