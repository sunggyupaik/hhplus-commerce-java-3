package com.hhplus.commerce.application.order.dto;

import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStatus;
import com.hhplus.commerce.domain.order.item.DeliveryStatus;
import com.hhplus.commerce.domain.order.item.OrderItem;
import com.hhplus.commerce.domain.order.item.OrderItemOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResultResponse {
    private List<OrderDetailResponse> result;

    public static OrderResultResponse of(List<OrderDetailResponse> orderDetailResponse) {
        return OrderResultResponse.builder()
                .result(orderDetailResponse)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailResponse {
        private OrderResponse order;
        private List<OrderItemSeries> orderItemSeries;

        public static OrderDetailResponse of(
                OrderResponse order,
                List<OrderItemSeries> orderItemSeries
        ) {
            return OrderDetailResponse.builder()
                    .order(order)
                    .orderItemSeries(orderItemSeries)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderResponse {
        private String receiverCity;
        private String receiverStreet;
        private String receiverZipcode;
        private OrderStatus orderStatus;

        public static OrderResponse of(Order order) {
            return OrderResponse.builder()
                    .receiverCity(order.getAddress().getReceiverCity())
                    .receiverStreet(order.getAddress().getReceiverStreet())
                    .receiverZipcode(order.getAddress().getReceiverZipcode())
                    .orderStatus(order.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemSeries {
        private OrderItemResponse orderItem;
        private OrderItemOptionResponse orderItemOption;

        public static OrderItemSeries of(OrderItemResponse orderItem, OrderItemOptionResponse orderItemOption) {
            return OrderItemSeries.builder()
                    .orderItem(orderItem)
                    .orderItemOption(orderItemOption)
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

        public static OrderItemResponse of(OrderItem orderItem) {
            return OrderItemResponse.builder()
                    .orderCount(orderItem.getOrderCount())
                    .itemName(orderItem.getItemName())
                    .itemPrice(orderItem.getItemPrice())
                    .deliveryStatus(orderItem.getDeliveryStatus())
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
