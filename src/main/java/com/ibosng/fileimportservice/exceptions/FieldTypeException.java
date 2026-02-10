package com.ibosng.fileimportservice.exceptions;

public class FieldTypeException extends Exception{

    public FieldTypeException(String message) {
        super(message);
    }

    public FieldTypeException(String message, Throwable error) {
        super(message, error);
    }
}
