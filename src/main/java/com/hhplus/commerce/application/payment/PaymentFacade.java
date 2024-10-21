package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.order.OrderStatusChangeService;
import com.hhplus.commerce.application.order.OrderDataPlatformSendService;
import com.hhplus.commerce.application.order.dto.OrderResponse;
import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.application.point.PointUseService;
import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private final PointUseService pointUseService;
    private final PaymentCreateService paymentCreateService;
    private final OrderStatusChangeService orderStatusChangeService;
    private final OrderDataPlatformSendService orderDataPlatformSendService;

    @Transactional
    public Long payOrder(Order order, PaymentRequest paymentRequest) {
        //포인트 차감
        PointRequest pointRequest = PointRequest.builder()
                .amount(paymentRequest.getAmount())
                .build();

        Long leftPoint = pointUseService.usePoint(paymentRequest.getCustomerId(), pointRequest);

        //결제
        paymentCreateService.createPayment(order, paymentRequest);

        // 주문 완료
        orderStatusChangeService.changeToComplete(order);

        // 데이터 플랫폼 전송
        orderDataPlatformSendService.send(OrderResponse.of(order));

        return leftPoint;
    }
}
