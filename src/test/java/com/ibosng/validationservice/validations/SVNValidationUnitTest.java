package com.ibosng.validationservice.validations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SVNValidationUnitTest {

    private static final LocalDate BIRTH_DATE = LocalDate.of(1997, 3, 19);

    @Test
    void validSocialSecurityNumber_returnsInputValue() {
        String validSVN = "9541190397";
        String result = SVNValidation.validateSVN(validSVN, BIRTH_DATE);

        assertNotNull(result);
        assertEquals(validSVN, result);
    }
    @Test
    void quadrupleZeroPrefix_returnsInputValue() {
        String zeroPrefixSVN = "0000190397";
        String result = SVNValidation.validateSVN(zeroPrefixSVN, BIRTH_DATE);

        assertNotNull(result);
        assertEquals(zeroPrefixSVN, result);
    }

    @Test
    void removesSpacesAndReturnsFormattedInputValue() {
        String svnWithSpaces = "9541 19 03 97";
        String result = SVNValidation.validateSVN(svnWithSpaces, BIRTH_DATE);

        assertNotNull(result);
        assertEquals(svnWithSpaces.replace(" ", ""), result);
    }

    @Test
    void unknownBirthday_returnsInputValue() {
        String svnWithUnknownBirthday = "6950191397";
        String result = SVNValidation.validateSVN(svnWithUnknownBirthday, null);

        assertNotNull(result);
        assertEquals(svnWithUnknownBirthday, result);
    }

    @Test
    void null_returnsNull() {
        String nullSVN = null;
        String result = SVNValidation.validateSVN(nullSVN, BIRTH_DATE);
        assertNull(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void emptyStringOrWhitespace_returnsNull(String svn) {
        String result = SVNValidation.validateSVN(svn, BIRTH_DATE);
        assertNull(result);
    }

    @Test
    void invalidChecksum_returnsNull() {
        String invalidSVN = "9541190396";
        String result = SVNValidation.validateSVN(invalidSVN, BIRTH_DATE);
        assertNull(result);
    }
}