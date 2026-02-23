package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerTitelValidation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TeilnehmerTitelValidationTest extends BaseIntegrationTest {

    @Test
    void testExecuteValidation_validTitel() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setTitel("Dipl_Ing");
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerTitelValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void testExecuteValidation_validMultipleTitel() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setTitel("M.A., B.A.");
        teilnehmer.setErrors(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerTitelValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

/*    @Test
    void testExecuteValidation_invalidTitelSpecialCharacters() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setTitel("Dr*");
        teilnehmer.setDataStatuses(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerTitelValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getDataStatuses().size());
        assertEquals(TeilnehmerDataStatus.INVALID_TITEL, teilnehmer.getDataStatuses().get(0));
    }

    @Test
    void testExecuteValidation_invalidTitel1() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setTitel("Drrrr");
        teilnehmer.setDataStatuses(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerTitelValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getDataStatuses().size());
        assertEquals(TeilnehmerDataStatus.INVALID_TITEL, teilnehmer.getDataStatuses().get(0));
    }

    @Test
    void testExecuteValidation_invalidTitel2() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setTitel("InvalidTitle");
        teilnehmer.setDataStatuses(new ArrayList<>());
        Validation<TeilnehmerStaging, Teilnehmer> validation = new TeilnehmerTitelValidation();

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getDataStatuses().size());
        assertEquals(TeilnehmerDataStatus.INVALID_TITEL, teilnehmer.getDataStatuses().get(0));
    }*/
}

