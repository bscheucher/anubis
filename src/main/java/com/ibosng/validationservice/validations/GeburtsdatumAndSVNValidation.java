package com.ibosng.validationservice.validations;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.isValidDate;
import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.validationservice.utils.ValidationHelpers.*;

public class GeburtsdatumAndSVNValidation {

    public static boolean validateGeburtsdatumAndSVN(String svnInput, LocalDate geburtsdatum) {
        return svnMatchesBirthday(svnInput, geburtsdatum);
    }

    private static boolean svnMatchesBirthday(String svnInput, LocalDate geburtsdatum) {
        if (geburtsdatum != null) {
            // Extract the last 6 digits from the SVN
            String dateString = svnInput.substring(4);
            if (isValidDate(dateString)) {
                LocalDate dateFromSVN = parseDate(dateString);

                // Check if the year is logically in the past (handling century crossover)
                if (dateFromSVN.isAfter(LocalDate.now())) {
                    dateFromSVN = dateFromSVN.minusYears(100);
                }
                return dateFromSVN.equals(geburtsdatum);
            } else {
                return isUnknownBirthday(svnInput);
            }
        }
        return true;
    }
}
