package com.ibosng.lhrservice.exceptions;

import lombok.Getter;

@Getter
public class LHRException extends RuntimeException {

    public LHRException(String message, Exception ex) {
        super(message, ex);
    }

    public LHRException(String message) {
        super(message);
    }
}
