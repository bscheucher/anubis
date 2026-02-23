package com.ibosng.gatewayservice.config;

import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import lombok.Getter;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.List;

@EnableCaching
@Configuration
@Profile("!test")
public class CacheConfig {

    @Getter
    @Value("${gatewayCacheExpirationHours:24}")
    private Integer gatewayCacheExpirationHours;

    @Bean
    public CacheManager ehCacheManager(
            javax.cache.configuration.Configuration<String, List> stringListConfiguration,
            javax.cache.configuration.Configuration<String, PayloadResponse> stringPayloadResponseConfiguration
    ) {
        CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        CacheManager cacheManager = provider.getCacheManager();

        cacheManager.createCache("zeitbuhungenListCache", stringListConfiguration);
        cacheManager.createCache("masterdataCache", stringPayloadResponseConfiguration);

        return cacheManager;
    }

    @Bean
    public javax.cache.configuration.Configuration<String, List> stringListConfiguration() {
        CacheConfigurationBuilder<String, List> cacheConfigurationBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class, List.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(1, MemoryUnit.MB))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(getGatewayCacheExpirationHours())));

        return Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfigurationBuilder);
    }

    @Bean
    public javax.cache.configuration.Configuration<String, PayloadResponse> stringPayloadResponseConfiguration() {
        CacheConfigurationBuilder<String, PayloadResponse> cacheConfigurationBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class, PayloadResponse.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder().heap(640, MemoryUnit.KB))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(getGatewayCacheExpirationHours())));

        return Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfigurationBuilder);
    }
}

