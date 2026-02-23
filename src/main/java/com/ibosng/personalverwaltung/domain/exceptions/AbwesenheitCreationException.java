package com.ibosng.personalverwaltung.domain.exceptions;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@RequiredArgsConstructor
public class AbwesenheitCreationException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public AbwesenheitCreationException(@NotNull String message,  @NotNull HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
