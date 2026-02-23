package com.ibosng._config;

import com.ibosng.gatewayservice.utils.RedissonClientConfigFactory;
import com.redis.testcontainers.RedisContainer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class RedisTestConfig {

    private final RedissonClientConfigFactory redissonClientConfigFactory;

    @Value("${testcontainers.redis.port}")
    private String port;

    @Value("${testcontainers.redis.host}")
    private String host;

    @Value("${testcontainers.redis.password}")
    private String password;

    @Value("${testcontainers.redis.image}")
    private String image;

    public RedisTestConfig(RedissonClientConfigFactory redissonClientConfigFactory) {
        this.redissonClientConfigFactory = redissonClientConfigFactory;
    }

    @Bean
    public RedisContainer redisContainer() {
        var container = new RedisContainer(DockerImageName.parse(image))
                .withExposedPorts(Integer.valueOf(port));
        container.start();
        return container;
    }

    @Bean
    @DependsOn("redisContainer")
    public RedissonClient redissonClient(RedisContainer redisContainer) {
        var mappedPort = redisContainer.getMappedPort(Integer.parseInt(port));
        var config = redissonClientConfigFactory.from(host, mappedPort.toString(), password);
        return Redisson.create(config);
    }
}
