package com.ibosng.validationservice.validations;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.*;

public class GeburtsdatumValidation {

    public static GeburtsdatumValidationResult validateGeburtsdatum(String geburtsdatumInput) {
        if (isNullOrBlank(geburtsdatumInput)) {
            return GeburtsdatumValidationResult.error("Das Feld ist leer.");
        }

        LocalDate before13Years = LocalDate.now().minusYears(13);
        LocalDate olderThan80Years = LocalDate.now().minusYears(80);
        LocalDate geburtsdatum = isValidDate(geburtsdatumInput) ? parseDate(geburtsdatumInput) : null;

        if (geburtsdatum == null) {
            return GeburtsdatumValidationResult.error("Ungültiges Geburtsdatum.");
        }
        if (geburtsdatum.isAfter(before13Years)) {
            return GeburtsdatumValidationResult.error("Das Mindestalter ist 13 Jahre.");
        }
        if (geburtsdatum.isBefore(olderThan80Years)) {
            return GeburtsdatumValidationResult.error("Das Höchstalter ist 80 Jahre.");
        }


        return GeburtsdatumValidationResult.success(geburtsdatum);
    }
}
