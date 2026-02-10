package com.ibosng.dbservice.dtos;

import java.util.HashMap;
import java.util.Map;

public class ErrorDto {
    private Map<String, String> errors = new HashMap<>();

    public void addError(String field, String message) {
        errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
