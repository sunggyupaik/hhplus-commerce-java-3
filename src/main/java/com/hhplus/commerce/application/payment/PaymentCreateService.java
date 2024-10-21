package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentHistory;
import com.hhplus.commerce.domain.payment.PaymentMethod;
import com.hhplus.commerce.domain.payment.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCreateService {
    private final PaymentStore paymentStore;

    @Transactional
    public Long createPayment(Order order, PaymentRequest paymentRequest) {
        validate(order, paymentRequest);

        Payment payment = paymentRequest.toEntity();
        Payment createdPayment = paymentStore.savePayment(payment);

        PaymentHistory paymentHistory = from(paymentRequest, null);
        paymentStore.saveOrderPaymentHistory(paymentHistory);

        return createdPayment.getId();
    }

    private void validate(Order order, PaymentRequest paymentRequest) {
        if (!order.calculatePrice().equals(paymentRequest.getAmount())) {
            //paymentStore.saveOrderPaymentHistory(from(paymentRequest, ErrorCode.PAYMENT_INVALID_PRICE));
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_PRICE);
        }

        if (!order.getCustomerId().equals(paymentRequest.getCustomerId())) {
            //paymentStore.saveOrderPaymentHistory(from(paymentRequest, ErrorCode.PAYMENT_INVALID_CUSTOMER));
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_CUSTOMER);
        }

        if (!order.paymentAvailable()) {
            //paymentStore.saveOrderPaymentHistory(from(paymentRequest, ErrorCode.PAYMENT_ALREADY_FINISHED));
            throw new IllegalStatusException(ErrorCode.PAYMENT_ALREADY_FINISHED);
        }
    }

    private PaymentHistory from(
            PaymentRequest paymentRequest,
            ErrorCode errorCode
    ) {
        return PaymentHistory.builder()
                .orderId(paymentRequest.getOrderId())
                .customerId(paymentRequest.getCustomerId())
                .paymentMethod(PaymentMethod.valueOf(paymentRequest.getPaymentMethod()))
                .amount(paymentRequest.getAmount())
                .code(errorCode == null ? "SUCCESS" : errorCode.name())
                .message(errorCode == null ? "SUCCESS" : errorCode.getErrorMsg())
                .build();
    }
}
