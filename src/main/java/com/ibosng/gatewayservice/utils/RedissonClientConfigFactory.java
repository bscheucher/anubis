package com.ibosng.gatewayservice.utils;

import org.jetbrains.annotations.NotNull;
import org.redisson.config.Config;
import org.redisson.config.SslVerificationMode;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RedissonClientConfigFactory {

   private final Environment environment;

    public RedissonClientConfigFactory(Environment environment) {
        this.environment = environment;
    }

    public Config from(String host, String port, String password) {

        if (host == null || host.isBlank() || port == null || port.isBlank()) {
            throw new IllegalStateException("Redis host/port not configured. Provide REDIS host/port or disable Redis usage.");
        }

        var redisAddress = getRedisAddress(host, port);

        var config = new Config();
        config.setCodec(new org.redisson.codec.JsonJacksonCodec());
        config.useSingleServer()
                .setAddress(redisAddress)
                .setSslVerificationMode(SslVerificationMode.NONE);


        if (password != null && !password.isBlank()) {
            config.useSingleServer().setPassword(password);
        }

        return config;
    }

    @NotNull
    private String getRedisAddress(String host, String port) {
        var activeProfiles = Arrays.asList(environment.getActiveProfiles());
        boolean useSsl = !(activeProfiles.contains("localdev") || activeProfiles.contains("test"));
        var scheme = useSsl ? "rediss" : "redis";
        return String.format("%s://%s:%s", scheme, host, port);
    }
}
