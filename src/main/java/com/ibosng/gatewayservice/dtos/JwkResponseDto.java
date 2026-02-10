package com.ibosng.gatewayservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class JwkResponseDto {

    @JsonProperty("keys")
    private List<JwkKey> keys;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JwkKey {

        @JsonProperty("kid")
        private String kid;

        @JsonProperty("nbf")
        private long nbf;

        @JsonProperty("use")
        private String use;

        @JsonProperty("kty")
        private String kty;

        @JsonProperty("e")
        private String e;

        @JsonProperty("n")
        private String n;
    }
}

