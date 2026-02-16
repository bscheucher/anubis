package com.ibosng.teilnehmerportal.exception;

import lombok.Getter;

@Getter
public class NatifApiException extends Exception {
    private final int statusCode;
    private final String responseBody;

    public NatifApiException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
}