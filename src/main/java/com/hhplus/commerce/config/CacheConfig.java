package com.hhplus.commerce.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {
    private static final Long ONE_MINUTE = 1L;
    private static final Long THIRTY_MINUTE = 30L;
    private final RedisConnectionFactory connectionFactory;

    @Bean
    @Primary
    public CacheManager cacheManager() {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheManager())
                .withInitialCacheConfigurations(confMap())
                .build();
    }

    // 테스트용
    private Map<String, RedisCacheConfiguration> confMap() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("bestItems:60", defaultCacheManager().entryTtl(Duration.ofSeconds(3)));
        cacheConfigurations.put("bestItems:60::bestItems", defaultCacheManager().entryTtl(Duration.ofSeconds(3)));
        return cacheConfigurations;
    }

    private RedisCacheConfiguration defaultCacheManager() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)))
                .entryTtl(Duration.ofMinutes(ONE_MINUTE));
    }

    @Bean
    public CacheManager thirtyMinutesCacheManager() {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(thirtyMinutesCache())
                .build();
    }

    private RedisCacheConfiguration thirtyMinutesCache() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)))
                .entryTtl(Duration.ofMinutes(THIRTY_MINUTE));
    }
}
