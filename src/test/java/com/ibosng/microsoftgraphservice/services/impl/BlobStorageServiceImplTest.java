package com.ibosng.microsoftgraphservice.services.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class BlobStorageServiceImplTest {

    private static final String ACCOUNT = "devstoreaccount1";
    private static final String KEY = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";
    private static final int BLOB_PORT = 10000;

    private static final String REPORTS_CONTAINER = "jasper-reports";
    private static final String FILES_CONTAINER = "docs";

    @Container
    static GenericContainer<?> azurite = new GenericContainer<>("mcr.microsoft.com/azure-storage/azurite")
            .withExposedPorts(BLOB_PORT)
            .withCommand("azurite --blobHost 0.0.0.0 --blobPort 10000");

    private BlobClientBuilder blobClientBuilder;
    private BlobContainerClientBuilder blobContainerClientBuilder;
    private BlobStorageServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        setupClientBuilders();

        // Ensure containers exist
        BlobContainerClient reportsClient = blobContainerClientBuilder.containerName(REPORTS_CONTAINER).buildClient();
        reportsClient.createIfNotExists();
        BlobContainerClient filesClient = blobContainerClientBuilder.containerName(FILES_CONTAINER).buildClient();
        filesClient.createIfNotExists();

        // Clean up any blobs from previous tests to ensure isolation
        reportsClient.listBlobs().forEach(item -> reportsClient.getBlobClient(item.getName()).delete());
        filesClient.listBlobs().forEach(item -> filesClient.getBlobClient(item.getName()).delete());

        service = new BlobStorageServiceImpl(blobClientBuilder, blobContainerClientBuilder);
        injectJasperReportsContainer();
    }

    private void injectJasperReportsContainer() throws NoSuchFieldException, IllegalAccessException {
        var field = BlobStorageServiceImpl.class.getDeclaredField("jasperReportsContainer");
        field.setAccessible(true);
        field.set(service, REPORTS_CONTAINER);
    }

    private void setupClientBuilders() {
        String host = azurite.getHost();
        Integer mapped = azurite.getMappedPort(BLOB_PORT);
        String endpoint = String.format("http://%s:%d/%s", host, mapped, ACCOUNT);
        StorageSharedKeyCredential cred = new StorageSharedKeyCredential(ACCOUNT, KEY);
        blobClientBuilder = new BlobClientBuilder().endpoint(endpoint).credential(cred);
        blobContainerClientBuilder = new BlobContainerClientBuilder().endpoint(endpoint).credential(cred);
    }

    @Test
    void uploadOrReplaceJasperReport() throws IOException {
        String name = "reports/sample.txt"; // blob path/name inside the container
        byte[] v1 = "first".getBytes(StandardCharsets.UTF_8);
        byte[] v2 = "second".getBytes(StandardCharsets.UTF_8);

        // Upload first version
        service.uploadOrReplaceJasperReport(name, new ByteArrayInputStream(v1), v1.length);
        // Upload replacement
        service.uploadOrReplaceJasperReport(name, new ByteArrayInputStream(v2), v2.length);

        BlobClient client = blobClientBuilder.containerName(REPORTS_CONTAINER).blobName(name).buildClient();
        byte[] downloaded = client.downloadContent().toBytes();
        assertEquals("second", new String(downloaded, StandardCharsets.UTF_8));
    }

    @Test
    void uploadOrReplaceFile() throws IOException {
        String container = FILES_CONTAINER;
        String original = "report_v1.pdf";
        String replacement = "report_v1.txt";
        byte[] v1 = "pdf version".getBytes(StandardCharsets.UTF_8);
        byte[] v2 = "text version".getBytes(StandardCharsets.UTF_8);

        // Seed an existing blob with a different extension
        BlobClient seed = blobClientBuilder.containerName(container).blobName(original).buildClient();
        seed.upload(new ByteArrayInputStream(v1), v1.length);
        assertTrue(seed.exists());

        // Now call the service to upload/replace with a different extension
        service.uploadOrReplaceFile(container, replacement, new ByteArrayInputStream(v2), v2.length);

        // Old blob should be gone, new blob should exist with new content
        BlobClient newClient = blobClientBuilder.containerName(container).blobName(replacement).buildClient();
        assertTrue(newClient.exists());
        byte[] downloaded = newClient.downloadContent().toBytes();
        assertEquals("text version", new String(downloaded, StandardCharsets.UTF_8));

        // Ensure no leftover old extension
        BlobClient oldClient = blobClientBuilder.containerName(container).blobName(original).buildClient();
        assertFalse(oldClient.exists());
    }

    @Test
    void downloadBlob() throws IOException {
        String container = FILES_CONTAINER;
        String name = "download/test.txt";
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);

        // Upload via low-level client
        BlobClient client = blobClientBuilder.containerName(container).blobName(name).buildClient();
        client.upload(new ByteArrayInputStream(data), data.length);

        // Download via service
        try (InputStream in = service.downloadBlob(name, container)) {
            byte[] buf = in.readAllBytes();
            assertEquals("hello world", new String(buf, StandardCharsets.UTF_8));
        }
    }

    @Test
    void getMatchingBlob() {
        String container = FILES_CONTAINER;
        String blob1 = "alpha.json";
        String blob2 = "beta.txt";
        BlobClient c1 = blobClientBuilder.containerName(container).blobName(blob1).buildClient();
        BlobClient c2 = blobClientBuilder.containerName(container).blobName(blob2).buildClient();
        c1.upload(new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)), 2);
        c2.upload(new ByteArrayInputStream("x".getBytes(StandardCharsets.UTF_8)), 1);

        // Query using identifier with extension; service trims extension internally
        var found = service.getMatchingBlob("alpha.json", container);
        assertNotNull(found);
        assertEquals(blob1, found.getName());
    }

    @Test
    void deleteFile() {
        String container = FILES_CONTAINER;
        String name = "beta.csv";
        BlobClient client = blobClientBuilder.containerName(container).blobName(name).buildClient();
        client.upload(new ByteArrayInputStream("data".getBytes(StandardCharsets.UTF_8)), 4);
        assertTrue(client.exists());

        // Delete by identifier without caring about extension
        service.deleteFile("beta", container);

        // Verify
        BlobContainerClient cont = blobContainerClientBuilder.containerName(container).buildClient();
        List<String> names = cont.listBlobs().stream().map(BlobItem::getName).collect(Collectors.toList());
        assertTrue(names.stream().noneMatch(n -> n.startsWith("beta")));
    }
}