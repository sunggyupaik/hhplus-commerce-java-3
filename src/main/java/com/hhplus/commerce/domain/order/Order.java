package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.support.BaseTimeEntity;
import com.hhplus.commerce.support.exception.IllegalStatusException;
import com.hhplus.commerce.support.response.ErrorCode;
import com.hhplus.commerce.domain.order.address.Address;
import com.hhplus.commerce.domain.order.item.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_status_created_date", columnList = "status, created_date"),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Order extends BaseTimeEntity {
    public static final int PAY_CHECK_MINUTE = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
    @ToString.Exclude
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
            Address address
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

    public Long calculatePrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::calculatePrice)
                .sum();
    }

    public boolean paymentAvailable() {
        return status == OrderStatus.INIT;
    }

    public void changeToOrderComplete() {
        if (status != OrderStatus.INIT) {
            throw new IllegalStatusException(ErrorCode.COMMON_ILLEGAL_STATUS);
        }

        status = OrderStatus.ORDER_COMPLETE;
    }

    public void changeToOrderCancel() {
        if (status == OrderStatus.CANCEL) {
            throw new IllegalStatusException(ErrorCode.COMMON_ILLEGAL_STATUS);
        }

        status = OrderStatus.CANCEL;
    }
}
