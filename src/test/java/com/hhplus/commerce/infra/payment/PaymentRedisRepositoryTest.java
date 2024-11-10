package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.config.cleaner.TearDownDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@TearDownDatabase
class PaymentRedisRepositoryTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PaymentRedisRepository paymentRedisRepository;

    @Test
    void opsForValueSet() {
        redisTemplate.opsForValue().set("hi", "bar");

        assertThat(redisTemplate.opsForValue().get("hi")).isEqualTo("bar");
    }

    @Test
    void setKeyValue() {
        paymentRedisRepository.setKeyValue("key", "value", 30L);

        Object value = paymentRedisRepository.findPaymentResponseByKey("key");

        assertThat(value).isEqualTo("value");
    }
}