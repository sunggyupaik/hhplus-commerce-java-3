package com.hhplus.commerce.config;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class RedisContainersConfig implements BeforeAllCallback {
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;

    @Container
    public GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                    .withExposedPorts(REDIS_PORT)
                    .withReuse(true);

    @Override
    public void beforeAll(ExtensionContext context) {
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT)));
    }
}
