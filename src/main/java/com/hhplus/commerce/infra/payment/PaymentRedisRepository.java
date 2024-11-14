package com.hhplus.commerce.infra.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PaymentRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public Object findPaymentResponseByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object setKeyValue(String key, Object value, Long expireMinute) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, expireMinute, TimeUnit.MINUTES);

        return value;
    }

    public Boolean setIfAbsent(String key, Object value, Long expireMinute) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofMinutes(expireMinute));
    }

    public String delete(String idempotencyKey) {
        redisTemplate.delete(idempotencyKey);
        return idempotencyKey;
    }
}
