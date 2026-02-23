package com.ibosng.moxisservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MoxisException extends RuntimeException {

    protected HttpStatus httpStatus;
    protected String body;

    public MoxisException(String message, HttpStatus httpStatus, String body, Exception ex) {
        super(message, ex);
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public MoxisException(String message, Exception ex) {
        super(message, ex);
    }

    public MoxisException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public MoxisException(Exception ex) {
        super(ex.getMessage(), ex);
    }

    public MoxisException() {
        super();
    }
}
