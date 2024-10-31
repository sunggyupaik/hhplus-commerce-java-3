package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.infra.customer.CustomerRepository;
import com.hhplus.commerce.infra.point.PointRepository;
import com.hhplus.commerce.infra.point.history.PointHistoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointChargeConcurrencyTest {
    @Autowired private PointChargeService pointChargeService;
    @Autowired private PointRepository pointRepository;
    @Autowired private PointHistoryRepository pointHistoryRepository;
    @Autowired private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        pointRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        pointHistoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("1명이 100원씩 10번을 동시에 충전하면 총 1000원이 충전된다")
    void concurrentChargeForSamePoint10times() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Customer savedCustomer = customerRepository.save(createCustomer());
        Point savedPoint = pointRepository.save(createPoint(savedCustomer.getId()));

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    PointRequest pointRequest = createPointChargeRequest(100L);
                    pointChargeService.chargePoint(savedCustomer.getId(), pointRequest);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(success.get()).isEqualTo(10);

        Long chargedPoint = pointRepository.findById(savedPoint.getId()).orElseThrow().getPoint();
        assertThat(chargedPoint).isEqualTo(1000L);
    }

    @Test
    @DisplayName("1명이 40000원씩 10번을 동시에 충전하면 2번 성공하고 8번 실패한다.")
    void concurrentChargeForSamePoint10timesOverMax() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        Customer savedCustomer = customerRepository.save(createCustomer());
        Point savedPoint = pointRepository.save(createPoint(savedCustomer.getId()));

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    PointRequest pointRequest = createPointChargeRequest(40000L);
                    pointChargeService.chargePoint(savedCustomer.getId(), pointRequest);
                    success.incrementAndGet();
                } catch (IllegalStatusException e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(success.get()).isEqualTo(2);
        assertThat(fail.get()).isEqualTo(8);
    }

    private PointRequest createPointChargeRequest(Long amount) {
        return PointRequest.builder()
                .amount(amount)
                .build();
    }

    private Point createPoint(Long customerId) {
        return Point.builder()
                .customerId(customerId)
                .point(0L)
                .build();
    }

    private Customer createCustomer() {
        return Customer.builder()
                .build();
    }
}
