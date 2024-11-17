package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.common.response.ErrorCode;
import com.hhplus.commerce.domain.order.Order;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentCommand;
import com.hhplus.commerce.domain.payment.PaymentInfo;
import com.hhplus.commerce.domain.payment.PaymentStore;
import com.hhplus.commerce.domain.payment.history.PaymentHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCreateService {
    private final PaymentStore paymentStore;

    @Transactional
    public PaymentInfo.PayOrderResponse createPayment(Order order, PaymentCommand.PayOrderRequest payOrderRequest) {
        validate(order, payOrderRequest);

        Payment payment = payOrderRequest.toEntity();
        Payment createdPayment = paymentStore.savePayment(payment);
        PaymentHistory paymentHistory = payOrderRequest.toPaymentHistoryEntity(null);
        if (paymentHistory.isSuccessHistory()) {
            paymentStore.saveOrderPaymentHistorySuccess(paymentHistory);
        }

        return PaymentInfo.PayOrderResponse.of(createdPayment);
    }

    private void validate(Order order, PaymentCommand.PayOrderRequest payOrderRequest) {
        if (!order.calculatePrice().equals(payOrderRequest.getAmount())) {
            paymentStore.saveOrderPaymentHistoryFail(
                    payOrderRequest.toPaymentHistoryEntity(ErrorCode.PAYMENT_INVALID_PRICE)
            );
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_PRICE);
        }

        if (!order.getCustomerId().equals(payOrderRequest.getCustomerId())) {
            paymentStore.saveOrderPaymentHistoryFail(
                    payOrderRequest.toPaymentHistoryEntity(ErrorCode.PAYMENT_INVALID_CUSTOMER)
            );
            throw new InvalidParamException(ErrorCode.PAYMENT_INVALID_CUSTOMER);
        }

        if (!order.paymentAvailable()) {
            paymentStore.saveOrderPaymentHistoryFail(
                    payOrderRequest.toPaymentHistoryEntity(ErrorCode.PAYMENT_ALREADY_FINISHED)
            );
            throw new IllegalStatusException(ErrorCode.PAYMENT_ALREADY_FINISHED);
        }
    }
}
