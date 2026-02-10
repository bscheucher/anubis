package com.ibosng.validationservice.validations;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GeburtsdatumValidationResult {
    private final LocalDate date;
    private final String errorMessage;

    private GeburtsdatumValidationResult(LocalDate date, String errorMessage) {
        this.date = date;
        this.errorMessage = errorMessage;
    }

    public static GeburtsdatumValidationResult success(LocalDate date) {
        return new GeburtsdatumValidationResult(date, null);
    }

    public static GeburtsdatumValidationResult error(String errorMessage) {
        return new GeburtsdatumValidationResult(null, errorMessage);
    }

    public boolean isValid() {
        return date != null;
    }
}

