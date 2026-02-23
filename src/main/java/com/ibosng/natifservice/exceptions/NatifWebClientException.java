package com.ibosng.natifservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
public class NatifWebClientException extends NatifException {

    HttpStatus httpStatus;

    public NatifWebClientException(String message, WebClientResponseException exception) {
        super(message, exception);
        this.httpStatus = HttpStatus.valueOf(exception.getStatusCode().value());
    }

    public NatifWebClientException(WebClientResponseException exception) {
        this(exception.getMessage(), exception);
    }
}
