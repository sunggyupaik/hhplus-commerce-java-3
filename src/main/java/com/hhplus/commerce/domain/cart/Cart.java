package com.hhplus.commerce.domain.cart;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private Long itemId;

    private Long itemOptionId;

    private Long quantity;

    @Builder
    public Cart(
            Long id,
            Long customerId,
            Long itemId,
            Long itemOptionId,
            Long quantity
    ) {
        this.id = id;
        this.customerId = customerId;
        this.itemId = itemId;
        this.itemOptionId = itemOptionId;
        this.quantity = quantity;
    }
}
