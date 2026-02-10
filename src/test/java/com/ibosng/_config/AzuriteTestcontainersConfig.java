package com.ibosng._config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * Testcontainers-backed Azurite configuration for tests.
 * Ensures an Azurite instance is running and exposes Spring properties
 * so that Azure SDK clients can connect during Spring context initialization.
 */
@Configuration
@Profile("test")
public class AzuriteTestcontainersConfig {

    @Container
    public static final GenericContainer<?> AZURITE = new GenericContainer<>("mcr.microsoft.com/azure-storage/azurite")
            .withExposedPorts(10000)
            .withCommand("azurite --blobHost 0.0.0.0 --blobPort 10000");

    static {
        // Ensure the container is started before Spring context property resolution
        AZURITE.start();
    }

}
