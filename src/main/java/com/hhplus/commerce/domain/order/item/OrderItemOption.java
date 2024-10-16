package com.hhplus.commerce.domain.order.item;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item_opionts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItemOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private OrderItem oderItem;

    private String itemOptionSize;

    private String itemOptionColor;

    private Long itemOptionPrice;

    @Builder
    public OrderItemOption(
            Long id,
            OrderItem oderItem,
            String itemOptionSize,
            String itemOptionColor,
            Long itemOptionPrice
    ) {
        this.id = id;
        this.oderItem = oderItem;
        this.itemOptionSize = itemOptionSize;
        this.itemOptionColor = itemOptionColor;
        this.itemOptionPrice = itemOptionPrice;
    }
}
