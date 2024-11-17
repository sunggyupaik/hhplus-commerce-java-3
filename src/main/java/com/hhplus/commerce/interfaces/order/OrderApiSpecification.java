package com.hhplus.commerce.interfaces.order;

import com.hhplus.commerce.common.response.CommonResponse;
import com.hhplus.commerce.domain.order.OrderStatus;
import com.hhplus.commerce.domain.order.address.Address;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "order", description = "주문 API")
public interface OrderApiSpecification {
    @Operation(summary = "주문 생성", description = "💡주어진 식별자와 주문 정보로 해당하는 주문을 생성합니다.")
    CommonResponse createOrder(
            @Parameter(description = "고객 식별자") Long customerId,
            @Parameter(description = "상품 정보") OrderDto.OrderRequest orderRequest
    );

    final class Fake implements OrderApiSpecification {
        @Override
        public CommonResponse createOrder(Long customerId, OrderDto.OrderRequest orderRequest) {
            Address address = Address.builder()
                    .receiverCity("서울")
                    .receiverStreet("달나무")
                    .receiverZipcode("123")
                    .build();

            OrderDto.OrderResponse orderResponse = OrderDto.OrderResponse.builder()
                    .orderId(10L)
                    .address(address)
                    .status(OrderStatus.INIT)
                    .totalPrice(2000L)
                    .build();

            return CommonResponse.success(orderResponse);
        }
    }
}
