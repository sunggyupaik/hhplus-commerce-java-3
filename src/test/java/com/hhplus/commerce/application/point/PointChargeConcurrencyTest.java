package com.hhplus.commerce.application.point;

import com.hhplus.commerce.application.point.dto.PointRequest;
import com.hhplus.commerce.common.exception.IllegalStatusException;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerStore;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointReader;
import com.hhplus.commerce.domain.point.PointStore;
import org.junit.jupiter.api.BeforeEach;
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
    private final PointChargeService pointChargeService;
    private final PointReader pointReader;
    private final PointStore pointStore;
    private final CustomerStore customerStore;

    public PointChargeConcurrencyTest(
            @Autowired PointChargeService pointChargeService,
            @Autowired PointReader pointReader,
            @Autowired PointStore pointStore,
            @Autowired CustomerStore customerStore
    ) {
        this.pointChargeService = pointChargeService;
        this.pointReader = pointReader;
        this.pointStore = pointStore;
        this.customerStore = customerStore;
    }

    @BeforeEach
    void setUp() {
        Customer customer = createCustomer(1L);
        customerStore.save(customer);

        Point point = createPoint(1L, 1L);
        pointStore.save(point);
    }

    @Test
    @DisplayName("1명이 100원씩 10번을 동시에 충전하면 총 1000원이 충전된다")
    void concurrentChargeForSamePoint10times() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    PointRequest pointRequest = createPointChargeRequest(100L);
                    pointChargeService.chargePoint(1L, pointRequest);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(success.get()).isEqualTo(10);
        assertThat(pointReader.getPoint(1L).getPoint()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("1명이 50000원씩 10번을 동시에 충전하면 2번 성공하고 8번 실패한다.")
    void concurrentCharegForSamePoint10timesOverMax() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger fail = new AtomicInteger(0);

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    PointRequest pointRequest = createPointChargeRequest(50000L);
                    pointChargeService.chargePoint(1L, pointRequest);
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

    private Point createPoint(Long id, Long customerId) {
        return Point.builder()
                .id(id)
                .customerId(customerId)
                .point(0L)
                .build();
    }

    private Customer createCustomer(Long id) {
        return Customer.builder()
                .id(id)
                .build();
    }
}
