package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerGeburtsdatumValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TeilnehmerGeburtsdatumValidationTest extends BaseIntegrationTest {

    private TeilnehmerGeburtsdatumValidation validation;
    private TeilnehmerStaging teilnehmerStaging;
    private Teilnehmer teilnehmer;

    @BeforeEach
    void setUp() {
        validation = new TeilnehmerGeburtsdatumValidation();
        teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());
    }

    @Test
    void testExecuteValidation_validGeburtsdatum() {
        teilnehmerStaging.setGeburtsdatum("02.01.2003");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void testExecuteValidation_invalidGeburtsdatum() {
        teilnehmerStaging.setGeburtsdatum("invalid-date");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("geburtsdatum", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    void testExecuteValidation_invalidGeburtsdatumYounger13Years() {
        teilnehmerStaging.setGeburtsdatum(LocalDate.now().minusYears(3).toString());

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("geburtsdatum", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    void testExecuteValidation_invalidGeburtsdatumOlderThan80Years() {
        teilnehmerStaging.setGeburtsdatum(LocalDate.now().minusYears(85).toString());

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("geburtsdatum", teilnehmer.getErrors().get(0).getError());
    }
}
