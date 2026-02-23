package com.ibosng.personalverwaltung.domain.exceptions;

import lombok.Getter;

@Getter
public class AbwesenheitCreationEmailException extends RuntimeException {

    public AbwesenheitCreationEmailException(String message) {
        super(message);
    }
}
