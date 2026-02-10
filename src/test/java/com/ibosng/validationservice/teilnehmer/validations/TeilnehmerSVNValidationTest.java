package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerGeburtsdatumAndSVNValidation;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerSVNValidation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static org.junit.jupiter.api.Assertions.*;

public class TeilnehmerSVNValidationTest extends BaseIntegrationTest {

    @Test
    void testExecuteValidation_validSVN() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setGeburtsdatum(parseDate("01.01.1990"));
        teilnehmerStaging.setSvNummer("5212010190"); // Set with a valid SV-Nummer
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerSVNValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void testExecuteValidation_missingSVN() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setGeburtsdatum(parseDate("01.01.1990"));
        teilnehmerStaging.setSvNummer(""); // Set with missing SV-Nummer
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerSVNValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertFalse(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void testExecuteValidation_invalidGeburstdatum() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setGeburtsdatum(parseDate("01.03.1990"));
        teilnehmerStaging.setSvNummer("5212010190"); // Set with a valid SV-Nummer
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerGeburtsdatumAndSVNValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(2, teilnehmer.getErrors().size());
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "svNummer".equals(error.getError())));
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "geburtsdatum".equals(error.getError())));
    }

    @Test
    public void validGeburtsdatum13() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setGeburtsdatum(parseDate("07.01.1998"));
        teilnehmerStaging.setSvNummer("3027071398"); // Set with a valid SV-Nummer
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerGeburtsdatumAndSVNValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertEquals(0, teilnehmer.getErrors().size());
    }

    @Test
    void testExecuteValidation_invalidSVNLength() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setSvNummer("12345678901");
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerSVNValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("svNummer", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    void testExecuteValidation_invalidSVNCheckSum() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setSvNummer("7378200999");
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerSVNValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("svNummer", teilnehmer.getErrors().get(0).getError());
    }
}
