package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.domain.order.OrderInfo;
import com.hhplus.commerce.domain.order.OrderStatus;
import com.hhplus.commerce.domain.order.address.Address;
import com.hhplus.commerce.domain.order.item.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Schema(description = "주문 요청")
    public static class OrderRequest {
        @Schema(description = "고객 식별자", example = "1")
        private Long customerId;

        @Schema(description = "수령자 도시", example = "서울")
        private String receiverCity;

        @Schema(description = "수령자 도로명", example = "새나무로")
        private String receiverStreet;

        @Schema(description = "수령자 우편번호", example = "123-1")
        private String receiverZipcode;

        @Schema(description = "주문 상품 요청 목록")
        private List<OrderItemRequest> orderItemList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Schema(description = "주문 상품 요청")
    public static class OrderItemRequest {
        @Schema(description = "상품 재고", example = "10")
        private Integer orderCount;

        @Schema(description = "상품 식별자", example = "20")
        private Long itemId;

        @Schema(description = "상품 이름", example = "겨울 코트")
        private String itemName;

        @Schema(description = "상품 가격", example = "10000")
        private Long itemPrice;

        @Schema(description = "배송 상태", example = "INIT")
        private String deliveryStatus;

        @Schema(description = "상품 옵션")
        private OrderItemOptionRequest orderItemOption;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Schema(description = "주문 상품 옵션 요청")
    public static class OrderItemOptionRequest {
        @Schema(description = "상품 옵션 식별자", example = "3")
        private Long itemOptionId;

        @Schema(description = "상품 옵션 사이즈", example = "95")
        private String itemOptionSize;

        @Schema(description = "상품 옵션 색깔", example = "빨강")
        private String itemOptionColor;

        @Schema(description = "상품 옵션 가격", example = "0")
        private Long itemOptionPrice;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Schema(description = "주문 응답")
    public static class OrderResponse {
        @Schema(description = "주문 식별자", example = "1")
        private Long orderId;

        @Schema(description = "주문 주소")
        private Address address;

        @Schema(description = "주문 상태", example = "INIT")
        private OrderStatus status;

        @Schema(description = "주문 총 금액", example = "20000")
        private Long totalPrice;

        public static OrderResponse of(OrderInfo.CreateResponse order) {
            return OrderResponse.builder()
                    .orderId(order.getOrderId())
                    .address(order.getAddress())
                    .status(order.getStatus())
                    .totalPrice(order.getTotalPrice())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "주문 목록 응답")
    public static class ResultResponse {
        @Schema(description = "주문 목록")
        private List<DetailResponse> result;

        public static ResultResponse of(
                List<OrderInfo.DetailResponse> detailResponse
        ) {
            return ResultResponse.builder()
                    .result(
                            detailResponse.stream()
                                    .map(DetailResponse::of)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "주문 상세 응답")
    public static class DetailResponse {
        @Schema(description = "주문 식별자", example = "1")
        private Long orderId;

        @Schema(description = "주소")
        private Address address;

        @Schema(description = "주문 상태", example = "INIT")
        private OrderStatus status;

        @Schema(description = "주문 총 금액", example = "20000")
        private Long totalPrice;

        @Schema(description = "주문 상품 목록 응답")
        private List<OrderItemResponse> orderItemList;

        public static DetailResponse of(
                OrderInfo.DetailResponse detail
        ) {
            return DetailResponse.builder()
                    .orderId(detail.getOrderId())
                    .address(detail.getAddress())
                    .status(detail.getStatus())
                    .totalPrice(detail.getTotalPrice())
                    .orderItemList(
                            detail.getOrderItemList().stream()
                                    .map(OrderItemResponse::of)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "주문 상세 응답")
    public static class OrderItemResponse {
        @Schema(description = "주문 상품 갯수", example = "5")
        private Integer orderCount;

        @Schema(description = "주문 상품 이름", example = "코트")
        private String itemName;

        @Schema(description = "주문 상품 금액", example = "20000")
        private Long itemPrice;

        @Schema(description = "주문 상품 상태", example = "BEFORE_DELIVERY")
        private DeliveryStatus deliveryStatus;

        @Schema(description = "주문 상품 옵션")
        private OrderItemOptionResponse orderItemOption;

        public static OrderItemResponse of(
                OrderInfo.OrderItemResponse orderItem
        ) {
            return OrderItemResponse.builder()
                    .orderCount(orderItem.getOrderCount())
                    .itemName(orderItem.getItemName())
                    .itemPrice(orderItem.getItemPrice())
                    .deliveryStatus(orderItem.getDeliveryStatus())
                    .orderItemOption(OrderItemOptionResponse.of(orderItem.getOrderItemOption()))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "주문 상품 옵션 응답")
    public static class OrderItemOptionResponse {
        @Schema(description = "주문 상품 옵션 사이즈")
        private String itemOptionSize;

        @Schema(description = "주문 상품 옵션 색깔")
        private String itemOptionColor;

        @Schema(description = "주문 상품 옵션 가격")
        private Long itemOptionPrice;

        public static OrderItemOptionResponse of(OrderInfo.OrderItemOptionResponse orderItemOption) {
            return OrderItemOptionResponse.builder()
                    .itemOptionSize(orderItemOption.getItemOptionSize())
                    .itemOptionColor(orderItemOption.getItemOptionColor())
                    .itemOptionPrice(orderItemOption.getItemOptionPrice())
                    .build();
        }
    }
}
