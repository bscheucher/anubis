package com.ibosng.moxisservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MoxisAuthException extends MoxisException {

    public MoxisAuthException(String message, IllegalArgumentException ex) {
        super(message, HttpStatus.UNAUTHORIZED, "", ex);
    }
}
