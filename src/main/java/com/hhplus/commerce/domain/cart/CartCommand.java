package com.hhplus.commerce.domain.cart;

import com.hhplus.commerce.interfaces.cart.CartDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CartCommand {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRequest {
        private Long customerId;
        private Long itemId;
        private Long itemOptionId;
        private Long quantity;

        public static AddRequest of(Long customerId, CartDto.AddRequest request) {
            return AddRequest.builder()
                    .customerId(customerId)
                    .itemId(request.getItemId())
                    .itemOptionId(request.getItemOptionId())
                    .quantity(request.getQuantity())
                    .build();
        }

        public Cart toEntity(Long customerId) {
            return Cart.builder()
                    .customerId(customerId)
                    .itemId(itemId)
                    .itemOptionId(itemOptionId)
                    .quantity(quantity)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteRequest {
        private Long customerId;
        private List<Long> itemOptionIdList;

        public static DeleteRequest of(Long customerId, CartDto.DeleteRequest request) {
            return DeleteRequest.builder()
                    .customerId(customerId)
                    .itemOptionIdList(request.getItemOptionIds())
                    .build();
        }
    }
}
