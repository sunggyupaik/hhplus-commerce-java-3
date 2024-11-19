package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.domain.order.address.Address;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import com.hhplus.commerce.interfaces.order.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCommand {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class OrderDataPlatformRequest {
        private Long orderId;

        public static OrderDataPlatformRequest of(Long orderId) {
            return OrderDataPlatformRequest.builder()
                    .orderId(orderId)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class OrderRequest {
        private Long customerId;
        private String receiverCity;
        private String receiverStreet;
        private String receiverZipcode;
        private List<OrderItemRequest> orderItemList;

        public static OrderRequest of(Long customerId, OrderDto.OrderRequest request) {
            return OrderRequest.builder()
                    .customerId(customerId)
                    .receiverCity(request.getReceiverCity())
                    .receiverStreet(request.getReceiverStreet())
                    .receiverZipcode(request.getReceiverZipcode())
                    .orderItemList(
                            request.getOrderItemList().stream()
                                    .map(OrderItemRequest::of)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }

        public Order toEntity(Long customerId) {
            Address address = Address.builder()
                    .receiverCity(receiverCity)
                    .receiverStreet(receiverStreet)
                    .receiverZipcode(receiverZipcode)
                    .build();

            return Order.builder()
                    .customerId(customerId)
                    .address(address)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class OrderItemRequest {
        private Integer orderCount;
        private Long itemId;
        private String itemName;
        private Long itemPrice;
        private String deliveryStatus;
        private OrderItemOptionRequest orderItemOption;

        public static OrderItemRequest of(OrderDto.OrderItemRequest request) {
            return OrderItemRequest.builder()
                    .orderCount(request.getOrderCount())
                    .itemId(request.getItemId())
                    .itemName(request.getItemName())
                    .itemPrice(request.getItemPrice())
                    .deliveryStatus(request.getDeliveryStatus())
                    .orderItemOption(OrderItemOptionRequest.of(request.getOrderItemOption()))
                    .build();
        }

        public OrderItem toEntity(Order order) {
            return OrderItem.builder()
                    .order(order)
                    .orderCount(orderCount)
                    .itemId(itemId)
                    .itemName(itemName)
                    .itemPrice(itemPrice)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class OrderItemOptionRequest {
        private Long itemOptionId;
        private String itemOptionSize;
        private String itemOptionColor;
        private Long itemOptionPrice;

        public static OrderItemOptionRequest of(OrderDto.OrderItemOptionRequest request) {
            return OrderItemOptionRequest.builder()
                    .itemOptionId(request.getItemOptionId())
                    .itemOptionSize(request.getItemOptionSize())
                    .itemOptionColor(request.getItemOptionColor())
                    .itemOptionPrice(request.getItemOptionPrice())
                    .build();
        }

        public OrderItemOption toEntity(OrderItem orderItem) {
            return OrderItemOption.builder()
                    .orderItem(orderItem)
                    .itemOptionSize(itemOptionSize)
                    .itemOptionColor(itemOptionColor)
                    .itemOptionPrice(itemOptionPrice)
                    .build();
        }
    }
}
