package com.hhplus.commerce.infra.payment;

import com.hhplus.commerce.config.RedisContainersConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RedisContainersConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentRedisRepositoryTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PaymentRedisRepository paymentRedisRepository;

    @Test
    void hello() {
        String fooResource = "/foo";

        testRestTemplate.put(fooResource, "bar");

        assertThat(testRestTemplate.getForObject(fooResource, String.class))
                .as("value is set")
                .isEqualTo("bar");
    }

    @Test
    void hello2() {
        redisTemplate.opsForValue().set("hi", "bar");

        assertThat(redisTemplate.opsForValue().get("hi")).isEqualTo("bar");
    }

    @Test
    void hello3() {
        paymentRedisRepository.setKeyValue("key", "value", 30L);

        Object value = paymentRedisRepository.findPaymentResponseByKey("key");

        assertThat(value).isEqualTo("value");
    }
}