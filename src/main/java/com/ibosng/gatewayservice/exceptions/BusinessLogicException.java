package com.ibosng.gatewayservice.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@RequiredArgsConstructor
public class BusinessLogicException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public BusinessLogicException(String message) {
        super(message);
        this.httpStatus = HttpStatus.OK;
        this.message = message;
    }
}
