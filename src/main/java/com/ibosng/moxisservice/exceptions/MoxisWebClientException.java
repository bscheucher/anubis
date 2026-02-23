package com.ibosng.moxisservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
public class MoxisWebClientException extends MoxisException {

    public MoxisWebClientException(WebClientResponseException ex) {
        super(ex.getMessage(), HttpStatus.valueOf(ex.getStatusCode().value()), ex.getResponseBodyAsString(), ex);
    }

    public MoxisWebClientException(WebClientRequestException ex) {
        super(ex.getMessage(), ex);
    }
}
