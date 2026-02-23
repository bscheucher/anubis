package com.ibosng.validationservice.validations;

import org.apache.commons.validator.routines.EmailValidator;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

public class EmailValidation {
    public static boolean isEmailValid(String inputEmail) {
        if (!isNullOrBlank(inputEmail)) {
            EmailValidator validator = EmailValidator.getInstance();
            return validator.isValid(inputEmail);
        }
        return false;
    }
}