package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.common.BaseTimeEntity;
import com.hhplus.commerce.domain.order.address.Address;
import com.hhplus.commerce.domain.order.item.OrderItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Long customerId;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    public Order(
            Long id,
            Long customerId,
            Address address,
            OrderStatus status
    ) {
        this.id = id;
        this.customerId = customerId;
        this.address = address;
        this.status = OrderStatus.INIT;
    }

    public Order addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        return this;
    }
}
