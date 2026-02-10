package com.ibosng.lhrservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
public class LHRWebClientException extends LHRException {

    private final HttpStatus httpStatus;

    public LHRWebClientException(String message, WebClientResponseException ex) {
        super(message, ex);
        this.httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
    }

    public LHRWebClientException(WebClientResponseException ex) {
        this(ex.getMessage(), ex);
    }

    public LHRWebClientException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
