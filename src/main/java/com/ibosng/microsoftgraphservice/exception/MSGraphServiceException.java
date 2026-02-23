package com.ibosng.microsoftgraphservice.exception;

import lombok.Getter;

@Getter
public class MSGraphServiceException extends Exception {

    public MSGraphServiceException(String message) {
        super(message);
    }

    public MSGraphServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
