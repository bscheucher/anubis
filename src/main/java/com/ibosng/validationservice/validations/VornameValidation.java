package com.ibosng.validationservice.validations;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

public class VornameValidation {
    public static boolean isVornameValid(String inputvorname) {
        if (!isNullOrBlank(inputvorname) && inputvorname.matches("^[\\p{L} .\\-\\']+$")) {
            return true;
        }
        return false;
    }
}