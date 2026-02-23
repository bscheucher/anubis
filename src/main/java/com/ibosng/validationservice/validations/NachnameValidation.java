package com.ibosng.validationservice.validations;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

public class NachnameValidation {
    public static boolean isNachnameValid(String inputNachname) {
        if (!isNullOrBlank(inputNachname) && inputNachname.matches("^[\\p{L} .\\-\\'()]+$")) {
            return true;
        }
        return false;
    }
}