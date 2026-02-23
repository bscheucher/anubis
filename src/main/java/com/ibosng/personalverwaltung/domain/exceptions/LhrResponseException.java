package com.ibosng.personalverwaltung.domain.exceptions;

import lombok.Getter;

@Getter
public class LhrResponseException extends RuntimeException {

    public LhrResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
