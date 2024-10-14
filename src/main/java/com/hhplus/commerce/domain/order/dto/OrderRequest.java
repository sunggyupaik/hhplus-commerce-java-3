package com.hhplus.commerce.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "주문 요청")
public class OrderRequest {
    @Schema(description = "고객 식별자", example = "1")
    private Long customerId;

    @Schema(description = "수령자 도시", example = "서울")
    private String receiverCity;

    @Schema(description = "수령자 도로명", example = "새나무로")
    private String receiverStreet;

    @Schema(description = "수령자 우편번호", example = "123-1")
    private String receiverZipcode;

    @Schema(description = "주문 상품 요청")
    private List<OrderItemRequest> orderItemRequestList;

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    @Schema(description = "주문 상품 요청")
    public static class OrderItemRequest {
        @Schema(description = "상품 재고", example = "10")
        private Long orderCount;

        @Schema(description = "상품 식별자", example = "20")
        private Long itemId;

        @Schema(description = "상품 이름", example = "겨울 코트")
        private String itemName;

        @Schema(description = "상품 가격", example = "10000")
        private Long itemPrice;

        @Schema(description = "배송 상태", example = "INIT")
        private String deliveryStatus;

        @Schema(description = "상품 옵션")
        private OrderItemOptionRequest orderItemOptionRequest;
    }

    @Builder
    @Setter
    @Getter
    @AllArgsConstructor
    @Schema(description = "주문 상품 옵션 요청")
    public static class OrderItemOptionRequest {
        @Schema(description = "상품 옵션 사이즈", example = "95")
        private String itemOptionSize;

        @Schema(description = "상품 옵션 색깔", example = "빨강")
        private String itemOptionColor;

        @Schema(description = "상품 옵션 가격", example = "0")
        private Long itemOptionPrice;
    }
}
