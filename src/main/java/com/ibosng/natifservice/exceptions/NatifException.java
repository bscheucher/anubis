package com.ibosng.natifservice.exceptions;

public class NatifException extends RuntimeException {
    public NatifException(String message, Exception exception) {
        super(message, exception);
    }
}
