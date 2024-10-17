package com.hhplus.commerce.domain.order.item;

import com.hhplus.commerce.domain.order.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private Order order;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "orderItem", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private OrderItemOption orderItemOption;

    private Integer orderCount;

    private Long itemId;

    private String itemName;

    private Long itemPrice;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Builder
    public OrderItem(
            Long id,
            Order order,
            Integer orderCount,
            Long itemId,
            String itemName,
            Long itemPrice
    ) {
        this.id = id;
        this.order = order;
        this.orderCount = orderCount;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.deliveryStatus = DeliveryStatus.BEFORE_DELIVERY;
    }

    public OrderItem changeOrderItemOption(OrderItemOption orderItemOption) {
        this.orderItemOption = orderItemOption;
        return this;
    }

    public Long calculatePrice() {
        return (itemPrice + orderItemOption.calculatePrice()) * orderCount;
    }
}
