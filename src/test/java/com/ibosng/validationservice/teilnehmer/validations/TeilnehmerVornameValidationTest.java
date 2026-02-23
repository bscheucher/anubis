package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerVornameValidation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TeilnehmerVornameValidationTest extends BaseIntegrationTest {

    @Test
    void testExecuteValidation_validVorname() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setVorname("Müller"); // Set with the valid surname
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerVornameValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void testExecuteValidation_invalidVorname() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setVorname("Müller3"); // Set with the valid surname
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerVornameValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());

        assertEquals("vorname", teilnehmer.getErrors().get(0).getError());
    }

}
