package com.ibosng.fileimportservice.exceptions;

public class ParserException extends Exception{

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable error) {
        super(message, error);
    }
}
