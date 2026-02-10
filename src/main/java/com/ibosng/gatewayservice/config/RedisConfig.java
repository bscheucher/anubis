package com.ibosng.gatewayservice.config;

import com.ibosng.gatewayservice.utils.RedissonClientConfigFactory;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Programmatic RedissonClient config kept in place for data codec configuration, which cannot be defined via application properties.
 * A YAML file could be used for file-based configuration, see: <a href="https://redisson.pro/docs/configuration/">...</a>
 * Note: Removed unused "redisSslEnabled" property.
 * */
@Configuration("lhrRedisConfig")
@Profile("!test")
public class RedisConfig {

    private final RedissonClientConfigFactory redissonClientConfigFactory;

    @Getter
    @Value("${azureRedisHost}")
    private String azureRedisHost;

    @Getter
    @Value("${azureRedisPassword}")
    private String azureRedisPassword;

    @Getter
    @Value("${azureRedisPort}")
    private String azureRedisPort;

    public RedisConfig(RedissonClientConfigFactory redissonClientConfigFactory) {
        this.redissonClientConfigFactory = redissonClientConfigFactory;
    }

    @Bean
    public RedissonClient redissonClient() {
        var config =  redissonClientConfigFactory.from(azureRedisHost, azureRedisPort, azureRedisPassword);
        return Redisson.create(config);
    }
}
