package com.ibosng.lhrservice.config;

import com.ibosng.lhrservice.exceptions.LHRException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("!test")
@Configuration
@Getter
public class LhrServiceWebClientConfig {

    @Value("${lhrUsername:#{null}}")
    private String lhrUsername;

    @Value("${lhrPassword:#{null}}")
    private String lhrPassword;

    @Value("${lhrUrl:#{null}}")
    private String lhrUrl;

    @Bean("lhrservicewebclient")
    public WebClient webClient() {
        try {
            return WebClient.builder()
                    .baseUrl(getLhrUrl())
                    .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(getLhrUsername(), getLhrPassword()))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new LHRException("Authentication has failed!", e);
        }
    }

}
