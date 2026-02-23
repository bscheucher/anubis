package com.ibosng.microsoftgraphservice.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("localdev")
@RequiredArgsConstructor
public class AzuriteBootstrap {


    private final BlobContainerClientBuilder blobContainerClientBuilder;

    // The container name you want to ensure exists
    @Value("${storageContainerJasperReports:#{null}}")
    private String jasperReportsContainer;

    @PostConstruct
    public void init() {
        BlobContainerClient containerClient = blobContainerClientBuilder
                .containerName(jasperReportsContainer)
                .buildClient();

        if (!containerClient.exists()) {
            containerClient.create();
            log.info("✅ Created blob container '{}'", jasperReportsContainer);
        } else {
            log.info("☑️  Blob container '{}' already exists", jasperReportsContainer);
        }
    }
}
