package com.ibosng._config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile({"test"})
@Configuration
public class LhrWebClientDummyConfig {

    @Bean("lhrservicewebclient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("dummy-url")
                .build();
    }
}