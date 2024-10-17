package com.hhplus.commerce.domain.customer;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Customer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Builder
    public Customer(
            Long id,
            String name,
            String email
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
