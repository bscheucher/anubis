package com.ibosng.validationservice.validations;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

public class AdresseValidation {
    public static boolean isAdresseValid(String inputAdresse) {
        if (!isNullOrBlank(inputAdresse) && inputAdresse.matches("^[\\p{L}\\d\\s/\\-\\.\\'\\(\\)\\\"\\+]+$")) {
            return true;
        }
        return false;
    }
}