package com.ibosng.usercreationservice.exception;

import lombok.Getter;

@Getter
public class TechnicalException extends Exception {

    public TechnicalException(String message) {
        super(message);
    }
}
