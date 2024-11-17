package com.hhplus.commerce.domain.order;

import com.hhplus.commerce.domain.order.address.Address;
import com.hhplus.commerce.domain.order.item.DeliveryStatus;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderInfo {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private Long orderId;
        private Address address;
        private OrderStatus status;
        private Long totalPrice;

        public static CreateResponse of(Order order) {
            return CreateResponse.builder()
                    .orderId(order.getId())
                    .address(order.getAddress())
                    .status(order.getStatus())
                    .totalPrice(order.calculatePrice())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultResponse {
        private List<DetailResponse> result;

        public static ResultResponse of(
                List<DetailResponse> detailResponse
        ) {
            return ResultResponse.builder()
                    .result(detailResponse)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponse {
        private Long orderId;
        private Address address;
        private OrderStatus status;
        private Long totalPrice;
        private List<OrderItemResponse> orderItemList;

        public static DetailResponse of(
                Order order,
                List<OrderItemResponse> orderItemList
        ) {
            return DetailResponse.builder()
                    .orderId(order.getId())
                    .address(order.getAddress())
                    .status(order.getStatus())
                    .totalPrice(order.calculatePrice())
                    .orderItemList(orderItemList)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Integer orderCount;
        private String itemName;
        private Long itemPrice;
        private DeliveryStatus deliveryStatus;
        private OrderItemOptionResponse orderItemOption;

        public static OrderItemResponse of(OrderItem orderItem, OrderItemOptionResponse orderItemOption) {
            return OrderItemResponse.builder()
                    .orderCount(orderItem.getOrderCount())
                    .itemName(orderItem.getItemName())
                    .itemPrice(orderItem.getItemPrice())
                    .deliveryStatus(orderItem.getDeliveryStatus())
                    .orderItemOption(orderItemOption)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemOptionResponse {
        private String itemOptionSize;
        private String itemOptionColor;
        private Long itemOptionPrice;

        public static OrderItemOptionResponse of(OrderItemOption orderItemOption) {
            return OrderItemOptionResponse.builder()
                    .itemOptionSize(orderItemOption.getItemOptionSize())
                    .itemOptionColor(orderItemOption.getItemOptionColor())
                    .itemOptionPrice(orderItemOption.getItemOptionPrice())
                    .build();
        }
    }
}
