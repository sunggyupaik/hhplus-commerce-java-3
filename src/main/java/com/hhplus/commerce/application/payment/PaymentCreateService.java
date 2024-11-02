package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.application.payment.dto.PaymentRequest;
import com.hhplus.commerce.application.payment.dto.PaymentResponse;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentHistory;
import com.hhplus.commerce.domain.payment.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCreateService {
    private final PaymentStore paymentStore;

    @Transactional
    public PaymentResponse createPayment(Order order, PaymentRequest paymentRequest) {
            validate(order, paymentRequest);

        Payment payment = paymentRequest.toEntity();
        Payment createdPayment = paymentStore.savePayment(payment);
        PaymentHistory paymentHistory = paymentRequest.toPaymentHistoryEntity(null);
        paymentStore.saveOrderPaymentHistory(paymentHistory);

        return PaymentResponse.of(createdPayment);
    }

    private void validate(Order order, PaymentRequest paymentRequest) {
        if (!order.calculatePrice().equals(paymentRequest.getAmount())) {
            paymentStore.saveOrderPaymentHistory(
                    paymentRequest.toPaymentHistoryEntity(ErrorCode.PAYMENT_INVALID_PRICE)
            );
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_PRICE);
        }

        if (!order.getCustomerId().equals(paymentRequest.getCustomerId())) {
            paymentStore.saveOrderPaymentHistory(
                    paymentRequest.toPaymentHistoryEntity(ErrorCode.PAYMENT_INVALID_CUSTOMER)
            );
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_CUSTOMER);
        }

        if (!order.paymentAvailable()) {
            paymentStore.saveOrderPaymentHistory(
                    paymentRequest.toPaymentHistoryEntity(ErrorCode.PAYMENT_ALREADY_FINISHED)
            );
            throw new IllegalStatusException(ErrorCode.PAYMENT_ALREADY_FINISHED);
        }
    }
}
