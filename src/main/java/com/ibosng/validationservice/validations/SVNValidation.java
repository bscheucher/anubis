package com.ibosng.validationservice.validations;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.isValidDate;
import static com.ibosng.validationservice.utils.Parsers.isLong;
import static com.ibosng.validationservice.utils.ValidationHelpers.isUnknownBirthday;

public class SVNValidation {

    public static String validateSVN(String svnInput, LocalDate geburtsdatum) {

        if (svnInput == null) {
            return null;
        }

        svnInput = svnInput.replace(" ", "");

        if (svnInput.matches("^[0-9]{10}$")) {
            if (svnInput.matches("^0000.*") ||
                    (isLong(svnInput) &&
                            calculateCheckSum(svnInput) == Character.getNumericValue(svnInput.charAt(3)) &&
                            (isValidDate(svnInput.substring(4)) ||
                                    isUnknownBirthday(svnInput)))) {
                return svnInput;
            }
        }

        return null;
    }

    private static int calculateCheckSum(String value) {
        int sum = Character.getNumericValue(value.charAt(0)) * 3
                + Character.getNumericValue(value.charAt(1)) * 7
                + Character.getNumericValue(value.charAt(2)) * 9
                + Character.getNumericValue(value.charAt(4)) * 5
                + Character.getNumericValue(value.charAt(5)) * 8
                + Character.getNumericValue(value.charAt(6)) * 4
                + Character.getNumericValue(value.charAt(7)) * 2
                + Character.getNumericValue(value.charAt(8)) * 1
                + Character.getNumericValue(value.charAt(9)) * 6;

        return sum % 11;
    }

}
