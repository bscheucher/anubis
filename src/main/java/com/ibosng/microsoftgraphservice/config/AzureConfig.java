package com.ibosng.microsoftgraphservice.config;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.file.share.ShareServiceClient;
import com.azure.storage.file.share.ShareServiceClientBuilder;
import com.ibosng.microsoftgraphservice.config.properties.AzureProperties;
import com.ibosng.microsoftgraphservice.config.properties.MailProperties;
import com.ibosng.microsoftgraphservice.config.properties.SSOProperties;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.Getter;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Getter
@Profile("!(localdev | test)")
@Configuration
@ComponentScan(basePackages = {"com.ibosng.microsoftgraphservice", "com.ibosng.dbservice"}) // Adjust this path
public class AzureConfig {

    @Value("${storageAccountName:#{null}}")
    private String accountName;

    @Value("${storageAccountKey:#{null}}")
    private String accountKey;

    public ClientSecretCredential clientSecretCredential(AzureProperties azureProperties) {
        return new ClientSecretCredentialBuilder()
                .clientId(azureProperties.getClientId())
                .clientSecret(azureProperties.getClientSecret())
                .tenantId(azureProperties.getTenantId())
                .build();
    }


    public GraphServiceClient<Request> graphClient(AzureProperties azureProperties, ClientSecretCredential clientSecretCredential) {
        return GraphServiceClient.builder()
                .authenticationProvider(
                        new TokenCredentialAuthProvider(
                                Collections.singletonList(azureProperties.getScope()),
                                clientSecretCredential))
                .buildClient();
    }

    @Bean
    @Qualifier("ssoGraphClient")
    public GraphServiceClient<Request> ssoGraphClient(SSOProperties ssoProperties) {
        return graphClient(ssoProperties, clientSecretCredential(ssoProperties));
    }

    @Bean
    @Qualifier("mailGraphClient")
    public GraphServiceClient<Request> mailGraphClient(MailProperties mailProperties) {
        return graphClient(mailProperties, clientSecretCredential(mailProperties));
    }

    @Bean
    @Qualifier("sharePointGraphClient")
    public GraphServiceClient<Request> sharePointGraphClient(SharePointProperties sharePointProperties) {
        return graphClient(sharePointProperties, clientSecretCredential(sharePointProperties));
    }

    @Bean
    public ShareServiceClient shareServiceClient() {
        return new ShareServiceClientBuilder()
                .endpoint(String.format("https://%s.file.core.windows.net", getAccountName()))
                .credential(new StorageSharedKeyCredential(getAccountName(), getAccountKey()))
                .buildClient();
    }

    @Bean
    public BlobClientBuilder blobClientBuilder() {
        return new BlobClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net", getAccountName()))
                .credential(new StorageSharedKeyCredential(getAccountName(), getAccountKey()));
    }

    @Bean
    public BlobContainerClientBuilder blobContainerClientBuilder() {
        return new BlobContainerClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net", getAccountName()))
                .credential(new StorageSharedKeyCredential(getAccountName(), getAccountKey()));
    }
}
