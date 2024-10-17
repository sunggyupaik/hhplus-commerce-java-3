package com.hhplus.commerce.domain.order.item;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_opionts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderItemOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
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

    public Long calculatePrice() {
        return itemOptionPrice;
    }
}
