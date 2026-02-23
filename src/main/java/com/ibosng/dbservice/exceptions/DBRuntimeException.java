package com.ibosng.dbservice.exceptions;

import lombok.Getter;


@Getter
public class DBRuntimeException extends RuntimeException {
    public DBRuntimeException(String message) {
        super(message);
    }

    public DBRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}