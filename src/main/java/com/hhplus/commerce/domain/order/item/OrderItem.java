package com.hhplus.commerce.domain.order.item;

import com.hhplus.commerce.domain.Item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Item item;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem", cascade = CascadeType.PERSIST)
    private List<OrderItemOption> orderItemOptions = new ArrayList<>();

    private Integer orderCount;

    private Long itemId;

    private String itemName;

    private Long itemPrice;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Builder
    public OrderItem(
            Long id,
            Item item,
            Integer orderCount,
            Long itemId,
            String itemName,
            Long itemPrice,
            DeliveryStatus deliveryStatus
    ) {
        this.id = id;
        this.item = item;
        this.orderCount = orderCount;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.deliveryStatus = DeliveryStatus.BEFORE_DELIVERY;
    }

    public OrderItem addOrderItemOption(OrderItemOption orderItemOption) {
        orderItemOptions.add(orderItemOption);
        return this;
    }
}
