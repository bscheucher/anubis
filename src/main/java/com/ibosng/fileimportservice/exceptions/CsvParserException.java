package com.ibosng.fileimportservice.exceptions;

public class CsvParserException extends ParserException{
    public CsvParserException(String message, Throwable error) {
        super(message, error);
    }

    public CsvParserException(String message) {
        super(message);
    }
}
