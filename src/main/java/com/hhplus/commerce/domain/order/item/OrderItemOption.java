package com.hhplus.commerce.domain.order.item;

import com.hhplus.commerce.common.BaseTimeEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "order_item_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderItemOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ToString.Exclude
    private OrderItem orderItem;

    private String itemOptionSize;

    private String itemOptionColor;

    private Long itemOptionPrice;

    @Builder
    public OrderItemOption(
            Long id,
            OrderItem orderItem,
            String itemOptionSize,
            String itemOptionColor,
            Long itemOptionPrice
    ) {
        this.id = id;
        this.orderItem = orderItem;
        this.itemOptionSize = itemOptionSize;
        this.itemOptionColor = itemOptionColor;
        this.itemOptionPrice = itemOptionPrice;
    }

    public Long calculatePrice() {
        return itemOptionPrice;
    }
}
