package com.ibosng.microsoftgraphservice.services.impl;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class AzuriteStandaloneTest {

    //public credentials used for Azurite
    private static final String ACCOUNT = "devstoreaccount1";
    private static final String KEY = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";
    private static final int BLOB_PORT = 10000;
    private static final String CONTAINER = "appdata";

    @Container
    static GenericContainer<?> azurite = new GenericContainer<>("mcr.microsoft.com/azure-storage/azurite")
            .withExposedPorts(BLOB_PORT)
            .withCommand("azurite --blobHost 0.0.0.0 --blobPort 10000");

    private BlobContainerClient container;

    @BeforeEach
    void setUp() {
        String host = azurite.getHost();
        Integer mapped = azurite.getMappedPort(BLOB_PORT);
        String endpoint = String.format("http://%s:%d/%s", host, mapped, ACCOUNT);

        StorageSharedKeyCredential cred = new StorageSharedKeyCredential(ACCOUNT, KEY);

        BlobContainerClientBuilder builder = new BlobContainerClientBuilder()
                .endpoint(endpoint)
                .credential(cred)
                .containerName(CONTAINER);

        container = builder.buildClient();
        container.createIfNotExists();
        assertThat(container.exists()).isTrue();
    }

    @Test
    void blobCrudTest() {
        String blobName = "hello/hello.txt";
        byte[] bytes = "hello azurite".getBytes(StandardCharsets.UTF_8);

        // upload
        container.getBlobClient(blobName).upload(new ByteArrayInputStream(bytes));

        // list
        var names = container.listBlobs().stream().map(BlobItem::getName).collect(Collectors.toSet());
        assertThat(names).contains(blobName);

        // download
        byte[] downloaded = container.getBlobClient(blobName).downloadContent().toBytes();
        assertThat(new String(downloaded, StandardCharsets.UTF_8)).isEqualTo("hello azurite");

        // delete
        container.getBlobClient(blobName).delete();
        names = container.listBlobs().stream().map(BlobItem::getName).collect(Collectors.toSet());
        assertThat(names).doesNotContain(blobName);
    }

    @Test
    void uploadEmptyContent_shouldCreateZeroLengthBlob() {
        String blobName = "empty/zero.txt";
        byte[] bytes = new byte[0];
        container.getBlobClient(blobName).upload(new ByteArrayInputStream(bytes));

        byte[] downloaded = container.getBlobClient(blobName).downloadContent().toBytes();
        assertThat(downloaded).isNotNull();
        assertThat(downloaded.length).isZero();

        container.getBlobClient(blobName).delete();
    }

    @Test
    void uploadLargeContent_shouldHandleSeveralMB() {
        String blobName = "large/5mb.bin";
        int size = 5 * 1024 * 1024; // 5 MB
        byte[] bytes = new byte[size];
        new Random(12345).nextBytes(bytes); // deterministic

        container.getBlobClient(blobName).upload(new ByteArrayInputStream(bytes));

        byte[] downloaded = container.getBlobClient(blobName).downloadContent().toBytes();
        assertThat(downloaded.length).isEqualTo(size);
        assertThat(downloaded).isEqualTo(bytes);

        container.getBlobClient(blobName).delete();
    }

    @Test
    void downloadNonExistentBlob_shouldThrowNotFound() {
        String blobName = "does/not/exist.txt";
        var client = container.getBlobClient(blobName);

        assertThatThrownBy(() -> client.downloadContent())
                .isInstanceOf(BlobStorageException.class)
                .extracting("statusCode")
                .isEqualTo(404);
    }

    @Test
    void uploadWithSpecialCharacters_shouldWork() {
        // Special characters should work
        String special = "special/üñîçødë !@#$%^&()[]{}=+~,;'.txt";
        byte[] specialBytes = "special".getBytes(StandardCharsets.UTF_8);
        container.getBlobClient(special).upload(new ByteArrayInputStream(specialBytes));
        byte[] downloaded = container.getBlobClient(special).downloadContent().toBytes();
        assertThat(new String(downloaded, StandardCharsets.UTF_8)).isEqualTo("special");
        container.getBlobClient(special).delete();
    }

    @Test
    void uploadWithInvalidName_emptyString_shouldReturnBadRequest() {
        // Invalid name: empty string should raise an error from service (400 Bad Request)
        String invalid = "";
        assertThatThrownBy(() -> container.getBlobClient(invalid)
                .upload(new ByteArrayInputStream("x".getBytes(StandardCharsets.UTF_8))))
                .isInstanceOf(BlobStorageException.class)
                .extracting("statusCode")
                .isEqualTo(400);
    }

    @Test
    void overwriteExistingBlob() {
        String blobName = "overwrite/sample.txt";
        byte[] first = "first".getBytes(StandardCharsets.UTF_8);
        byte[] second = "second".getBytes(StandardCharsets.UTF_8);

        var client = container.getBlobClient(blobName);
        var blockClient = client.getBlockBlobClient();

        // initial upload
        blockClient.upload(new ByteArrayInputStream(first), first.length);

        // try to upload again without overwrite should fail
        assertThatThrownBy(() -> blockClient.upload(new ByteArrayInputStream(second), second.length))
                .isInstanceOf(BlobStorageException.class);

        // overwrite=true should succeed
        blockClient.upload(new ByteArrayInputStream(second), second.length, true);

        byte[] downloaded = client.downloadContent().toBytes();
        assertThat(new String(downloaded, StandardCharsets.UTF_8)).isEqualTo("second");

        client.delete();
    }
}
