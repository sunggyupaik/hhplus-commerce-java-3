package com.hhplus.commerce.infra.order;

import com.hhplus.commerce.application.item.dto.ItemBestResponse;
import com.hhplus.commerce.common.exception.EntityNotFoundException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderReaderImpl implements OrderReader {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public List<ItemBestResponse> getBestItems() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(3);
        return orderItemRepository.findTop5ByOrderCountSum(startDate, endDate);
    }
}
