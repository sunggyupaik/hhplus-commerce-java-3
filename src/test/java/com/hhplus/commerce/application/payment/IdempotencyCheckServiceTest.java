package com.hhplus.commerce.application.payment;

import com.hhplus.commerce.common.exception.InvalidParamException;
import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.payment.Payment;
import com.hhplus.commerce.domain.payment.PaymentCommand;
import com.hhplus.commerce.domain.payment.PaymentMethod;
import com.hhplus.commerce.domain.payment.idempotency.PaymentIdempotency;
import com.hhplus.commerce.infra.customer.CustomerRepository;
import com.hhplus.commerce.infra.payment.PaymentIdempotencyRepository;
import com.hhplus.commerce.infra.payment.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@TearDownDatabase
class IdempotencyCheckServiceTest {
    @Autowired private IdempotencyCheckService idempotencyCheckService;

    @Autowired private CustomerRepository customerRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PaymentIdempotencyRepository paymentIdempotencyRepository;

    @Test
    @DisplayName("멱등성 키가 없으면 결제 요청을 실패한다")
    void idempotencyCheckWithIdempotencyNull() throws InterruptedException {
        PaymentCommand.PayOrderRequest paymentRequest = createPaymentRequest(
                1L, 1L, "TOSS", 15000L, null
        );

        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    idempotencyCheckService.idempotencyCheck(paymentRequest);
                    success.incrementAndGet();
                } catch (InvalidParamException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 0,
                "멱등성 키가 null이면 성공은 0건이다");
        Assertions.assertEquals(fail.get(), 10,
                "멱등성 키가 null이면 10건의 시도는 모두 실패한다");
    }

    @Test
    @DisplayName("멱등성 키는 같지만 요청 내용이 다르면 결제 요청을 실패한다")
    void idempotencyCheckWithSameKeyNotSamePayload() throws InterruptedException {
        paymentFixture(1L, 1L, "TOSS", 15000L);
        paymentIdempotencyFixture(1L, "123");
        Customer customer = customerFixture();

        PaymentCommand.PayOrderRequest paymentRequest = createPaymentRequest(
                1L, customer.getId(), "TOSS", 19000L, "123"
        );

        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    idempotencyCheckService.idempotencyCheck(paymentRequest);
                    success.incrementAndGet();
                } catch (InvalidParamException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 0,
                "멱등성 키가 같아도 요청 body가 다르면 성공은 0건이다");
        Assertions.assertEquals(fail.get(), 10,
                "멱등성 키가 같아도 요청 body가 다르면 10건의 시도는 모두 실패한다");
    }

    //customer
    private Customer customerFixture() {
        Customer customer = createCustomer();

        return customerRepository.save(customer);
    }

    private Customer createCustomer() {
        return Customer.builder()
                .build();
    }

    //payment
    private Payment paymentFixture(Long orderId, Long customerId, String paymentMethod, Long amount) {
        Payment payment = createPayment(orderId, customerId, paymentMethod, amount);

        return paymentRepository.save(payment);
    }

    private Payment createPayment(Long orderId, Long customerId, String paymentMethod, Long amount) {
        return Payment.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(PaymentMethod.valueOf(paymentMethod))
                .amount(amount)
                .build();
    }

    //idempotency
    private PaymentIdempotency paymentIdempotencyFixture(Long orderId, String idempotencyKey) {
        PaymentIdempotency paymentIdempotency = createPaymentIdempotency(orderId, idempotencyKey);

        return paymentIdempotencyRepository.save(paymentIdempotency);
    }

    private PaymentIdempotency createPaymentIdempotency(Long orderId, String idempotencyKey) {
        return PaymentIdempotency.builder()
                .orderId(orderId)
                .idempotencyKey(idempotencyKey)
                .build();
    }

    //payment
    private PaymentCommand.PayOrderRequest createPaymentRequest(
            Long orderId, Long customerId, String paymentMethod, Long amount, String idempotencyKey) {
        return PaymentCommand.PayOrderRequest.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .idempotencyKey(idempotencyKey)
                .build();
    }
}
