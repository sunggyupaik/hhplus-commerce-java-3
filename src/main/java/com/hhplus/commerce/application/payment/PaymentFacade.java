package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.order.OrderDataPlatformSendService;
import com.hhplus.commerce.application.order.OrderQueryService;
import com.hhplus.commerce.application.order.OrderStatusChangeService;
import com.hhplus.commerce.application.order.dataPlatform.OrderDataPlatformEvent;
import com.hhplus.commerce.application.order.dataPlatform.OrderDataPlatformPublisher;
import com.hhplus.commerce.application.payment.dto.PaymentIdempotencyCheckResponse;
import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.application.payment.dto.PaymentResponse;
import com.hhplus.commerce.application.point.PointUseService;
import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private static final Long EXPIRE_MINUTE_15 = 15L;
    private final PointUseService pointUseService;
    private final PaymentCreateService paymentCreateService;
    private final OrderQueryService orderQueryService;
    private final OrderStatusChangeService orderStatusChangeService;
    private final OrderDataPlatformSendService orderDataPlatformSendService;
    private final IdempotencyCheckService idempotencyCheckService;
    private final IdempotencyManageService idempotencyManageService;
    private final OrderDataPlatformPublisher orderDataPlatformPublisher;

    /**
     * 분산락과 멱등성 DB로 결제한다
     * @param paymentRequest 결제 요청
     * @return 결제 응답
     */
    @Transactional
    public PaymentResponse payOrder(PaymentRequest paymentRequest) {
        //멱등성 검사
        PaymentIdempotencyCheckResponse response = idempotencyCheckService.idempotencyCheck(paymentRequest);
        if (response.isIdempotencyKeyExists()) {
            return response.getPaymentResponse();
        }

        //포인트 차감
        pointUseService.usePoint(paymentRequest.getCustomerId(), PointRequest.of(paymentRequest.getAmount()));

        //결제 저장
        Order order = orderQueryService.getOrderWithPessimisticLock(paymentRequest.getOrderId());
        PaymentResponse paymentResponse = paymentCreateService.createPayment(order, paymentRequest);

        // 주문 완료
        orderStatusChangeService.changeToComplete(order);

        // 데이터 플랫폼 전송 이벤트
        orderDataPlatformPublisher.success(OrderDataPlatformEvent.of(order));

        return paymentResponse;
    }

    /**
     * 분산락과 Redis로 결제한다
     * 만약 type가 1이면 모든 로직이 끝나고 RuntimeException을 던진다
     * @param paymentRequest 결제 요청
     * @param type 예외 타입
     * @return 결제 응답
     */
    @Transactional
    public PaymentResponse payOrderRedis(PaymentRequest paymentRequest, String type) {
        //멱등성 검사
        PaymentIdempotencyCheckResponse response = idempotencyCheckService.idempotencyCheckRedis(paymentRequest);
        if (response.isIdempotencyKeyExists()) {
            return response.getPaymentResponse();
        }

        //결제 저장
        Order order = orderQueryService.getOrderWithPessimisticLock(paymentRequest.getOrderId());
        PaymentResponse paymentResponse = paymentCreateService.createPayment(order, paymentRequest);

        //포인트 차감
        pointUseService.usePoint(paymentRequest.getCustomerId(), PointRequest.of(paymentRequest.getAmount()));

        // 주문 완료
        orderStatusChangeService.changeToComplete(order);

        // 레디스에 <멱등성 키, 결제> 캐시 저장
        idempotencyManageService.saveIdempotencyPayment(
                paymentRequest.getIdempotencyKey(), paymentResponse, EXPIRE_MINUTE_15
        );

        // 데이터 플랫폼 전송 이벤트
        orderDataPlatformPublisher.success(OrderDataPlatformEvent.of(order));

        if (type.equals("1")) {
            throw new RuntimeException();
        }

        return paymentResponse;
    }
}
