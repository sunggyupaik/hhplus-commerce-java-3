package com.hhplus.commerce.application.point;

import com.hhplus.commerce.support.exception.IllegalStatusException;
import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.point.Point;
import com.hhplus.commerce.domain.point.PointCommand;
import com.hhplus.commerce.infra.customer.CustomerRepository;
import com.hhplus.commerce.infra.point.PointRepository;
import com.hhplus.commerce.infra.point.history.PointHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@TearDownDatabase
public class PointChargeConcurrencyTest {
    @Autowired private PointChargeService pointChargeService;
    @Autowired private PointRepository pointRepository;
    @Autowired private PointHistoryRepository pointHistoryRepository;
    @Autowired private CustomerRepository customerRepository;

    @Test
    @DisplayName("1명이 100원씩 10번을 동시에 충전하면 총 1000원이 충전된다 - 비관락")
    void concurrentChargeForSamePoint10timesPessimistic() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Customer savedCustomer = customerRepository.save(createCustomer());
        Point savedPoint = pointRepository.save(createPoint(savedCustomer.getId()));

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    PointCommand.ChargeRequest command = createPointChargeCommand(savedCustomer.getId(), 100L);
                    pointChargeService.chargePointWithPessimisticLock(command);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 10,
                "반환된 성공 횟수 10은 동시에 시도한 충전 횟수이다");

        Long chargedPoint = pointRepository.findById(savedPoint.getId()).orElseThrow().getPoint();
        Assertions.assertEquals(chargedPoint, 1000L,
                "반환된 1000원은 100원을 10번 충전한 결과이다");
    }

    @Test
    @DisplayName("1명이 100원씩 10번을 동시에 충전하면 총 1000원이 충전된다 - 낙관락")
    void concurrentChargeForSamePoint10timesOptimistic() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Customer savedCustomer = customerRepository.save(createCustomer());
        Point savedPoint = pointRepository.save(createPoint(savedCustomer.getId()));

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    PointCommand.ChargeRequest command = createPointChargeCommand(savedCustomer.getId(),100L);
                    pointChargeService.chargePointWithOptimisticLock(command);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 10,
                "반환된 성공 횟수 10은 동시에 시도한 충전 횟수이다");

        Long chargedPoint = pointRepository.findById(savedPoint.getId()).orElseThrow().getPoint();
        Assertions.assertEquals(chargedPoint, 1000L,
                "반환된 1000원은 100원을 10번 충전한 결과이다");
    }

    @Test
    @DisplayName("1명이 100원씩 10번을 동시에 충전하면 총 1000원이 충전된다 - 분산락")
    void concurrentChargeForSamePoint10timesDistributed() throws InterruptedException {
        final int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger success = new AtomicInteger(0);

        Customer savedCustomer = customerRepository.save(createCustomer());
        Point savedPoint = pointRepository.save(createPoint(savedCustomer.getId()));

        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    PointCommand.ChargeRequest command = createPointChargeCommand(savedCustomer.getId(), 100L);
                    pointChargeService.chargePointWithDistributedLock(command);
                    success.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Assertions.assertEquals(success.get(), 10,
                "반환된 성공 횟수 10은 동시에 시도한 충전 횟수이다");

        Long chargedPoint = pointRepository.findById(savedPoint.getId()).orElseThrow().getPoint();
        Assertions.assertEquals(chargedPoint, 1000L,
                "반환된 1000원은 100원을 10번 충전한 결과이다");
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
                    PointCommand.ChargeRequest command = createPointChargeCommand(savedCustomer.getId(),40000L);
                    pointChargeService.chargePointWithPessimisticLock(command);
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

    private PointCommand.ChargeRequest createPointChargeCommand(Long customerId, Long amount) {
        return PointCommand.ChargeRequest.builder()
                .customerId(customerId)
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
