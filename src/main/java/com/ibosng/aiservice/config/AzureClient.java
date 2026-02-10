package com.ibosng.aiservice.config;

import com.azure.ai.openai.assistants.AssistantsAsyncClient;
import com.azure.ai.openai.assistants.AssistantsClient;
import com.azure.ai.openai.assistants.AssistantsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureClient {

    @Getter
    @Value(("${azureOpenAIKey:#{null}}"))
    private String azureOpenAIKey;

    @Getter
    @Value(("${azureOpenAIEndPoint:#{null}}"))
    private String azureOpenAIEndPoint;


    @Bean
    public AssistantsClient assistantsClient() {
        return new AssistantsClientBuilder()
                .credential(new AzureKeyCredential(getAzureOpenAIKey()))
                .endpoint(getAzureOpenAIEndPoint())
                .buildClient();
    }

    @Bean
    public AssistantsAsyncClient assistantsAsyncClient() {
        return new AssistantsClientBuilder()
                .credential(new AzureKeyCredential(getAzureOpenAIKey()))
                .endpoint(getAzureOpenAIEndPoint())
                .buildAsyncClient();
    }
}
