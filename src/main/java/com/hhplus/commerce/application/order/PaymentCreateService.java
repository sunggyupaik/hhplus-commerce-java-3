package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.payment.OrderPayment;
import com.hhplus.commerce.domain.order.payment.OrderPaymentHistory;
import com.hhplus.commerce.domain.order.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCreateService {
    private final OrderStore orderStore;

    @Transactional
    public Long createPayment(Order order, PaymentRequest paymentRequest) {
        validate(order, paymentRequest);
        OrderPayment orderPayment = paymentRequest.toEntity(order);
        OrderPayment createdOrderPayment = orderStore.savePayment(orderPayment);

        OrderPaymentHistory orderPaymentHistory = from(order, paymentRequest, null);
        orderStore.saveOrderPaymentHistory(orderPaymentHistory);

        return createdOrderPayment.getId();
    }

    private void validate(Order order, PaymentRequest paymentRequest) {
        if (!order.calculatePrice().equals(paymentRequest.getAmount())) {
            OrderPaymentHistory orderPaymentHistory =
                    from(order, paymentRequest, ErrorCode.PAYMENT_INVALID_PRICE);

            orderStore.saveOrderPaymentHistory(orderPaymentHistory);
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_PRICE);
        }

        if (!order.getCustomerId().equals(paymentRequest.getCustomerId())) {
            OrderPaymentHistory orderPaymentHistory =
                    from(order, paymentRequest, ErrorCode.PAYMENT_INVALID_CUSTOMER);

            orderStore.saveOrderPaymentHistory(orderPaymentHistory);
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_CUSTOMER);
        }

        if (!order.paymentAvailable()) {
            OrderPaymentHistory orderPaymentHistory =
                    from(order, paymentRequest, ErrorCode.PAYMENT_INVALID_ORDER);

            orderStore.saveOrderPaymentHistory(orderPaymentHistory);
            throw new IllegalStatusException(ErrorCode.PAYMENT_INVALID_ORDER);
        }
    }

    private OrderPaymentHistory from(
            Order order,
            PaymentRequest paymentRequest,
            ErrorCode errorCode
    ) {
        return OrderPaymentHistory.builder()
                .order(order)
                .customerId(paymentRequest.getCustomerId())
                .paymentMethod(PaymentMethod.valueOf(paymentRequest.getPaymentMethod()))
                .amount(paymentRequest.getAmount())
                .code(errorCode == null ? "SUCCESS" : errorCode.name())
                .message(errorCode == null ? "SUCCESS" : errorCode.getErrorMsg())
                .build();
    }
}
