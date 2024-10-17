package com.hhplus.commerce.application.order;

import com.hhplus.commerce.application.order.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.order.OrderStore;
import com.hhplus.commerce.domain.order.payment.Payment;
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
        Payment payment = paymentRequest.toEntity(order);
        Payment createdPayment = orderStore.savePayment(payment);

        return createdPayment.getId();
    }

    private void validate(Order order, PaymentRequest paymentRequest) {
        if (!order.calculatePrice().equals(paymentRequest.getAmount())) {
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_PRICE);
        }

        if (!order.getCustomerId().equals(paymentRequest.getCustomerId())) {
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_CUSTOMER);
        }

        if (!order.paymentAvailable()) {
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_ORDER);
        }
    }
}
