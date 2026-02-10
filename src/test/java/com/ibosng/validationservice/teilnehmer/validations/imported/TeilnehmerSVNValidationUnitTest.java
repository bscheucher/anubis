package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeilnehmerSVNValidationUnitTest {

    private TeilnehmerSVNValidation validation;
    private static final LocalDate BIRTH_DATE = LocalDate.of(1999, 5, 10);
    private static final String TEST_USERNAME = "test.user";

    @BeforeEach
    void setUp() {
        validation = new TeilnehmerSVNValidation();
    }

    private TeilnehmerStaging createTestTeilnehmerStaging(String svn) {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSource(TeilnehmerSource.VHS);
        staging.setSvNummer(svn);
        return staging;
    }

    private Teilnehmer createTestTeilnehmer() {
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setGeburtsdatum(BIRTH_DATE);
        teilnehmer.setErrors(new ArrayList<>());
        return teilnehmer;
    }

    @Test
    void validSocialSecurityNumber_returnsTrue() {
        var svn = "4551051099";
        Teilnehmer teilnehmer = createTestTeilnehmer();
        TeilnehmerStaging staging = createTestTeilnehmerStaging(svn);
        boolean result = validation.executeValidation(staging, teilnehmer);

        assertTrue(result);
        assertEquals(svn, teilnehmer.getSvNummer());
    }

    @Test
    void null_returnsFalseAndAddsError() {
        Teilnehmer teilnehmer = createTestTeilnehmer();
        TeilnehmerStaging staging = createTestTeilnehmerStaging(null);

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("svNummer", teilnehmer.getErrors().get(0).getError());
        assertEquals("Das Feld ist leer", teilnehmer.getErrors().get(0).getCause());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void emptyOrWhitespace_returnsFalseAndAddsError(String svn) {
        Teilnehmer teilnehmer = createTestTeilnehmer();
        TeilnehmerStaging staging = createTestTeilnehmerStaging(svn);

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("svNummer", teilnehmer.getErrors().get(0).getError());
        assertEquals("Das Feld ist leer", teilnehmer.getErrors().get(0).getCause());
    }
}
