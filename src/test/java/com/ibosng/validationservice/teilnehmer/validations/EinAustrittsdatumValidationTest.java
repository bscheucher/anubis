package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.imported.EinAustrittsdatumValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class EinAustrittsdatumValidationTest extends BaseIntegrationTest {

    private EinAustrittsdatumValidation validation;
    private TeilnehmerStaging teilnehmerStaging;
    private Teilnehmer teilnehmer;
    private Teilnehmer2Seminar teilnehmer2Seminar;

    @BeforeEach
    void setUp() {
        validation = new EinAustrittsdatumValidation();
        teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());
        teilnehmer2Seminar = new Teilnehmer2Seminar();
        teilnehmer2Seminar.setTeilnehmer(teilnehmer);
    }

    @Test
    void whenBothDatesAreValidAndAustrittIsAfterEintritt_thenValidationSucceeds() {
        teilnehmerStaging.setEintritt("01.01.2023");
        teilnehmerStaging.setAustritt("02.01.2023");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void whenEintrittIsInvalid_thenValidationFails() {
        teilnehmerStaging.setEintritt("invalid-date");
        teilnehmerStaging.setAustritt("02.01.2023");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("eintritt", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    void whenAustrittIsInvalid_thenValidationFails() {
        teilnehmerStaging.setEintritt("01.01.2023");
        teilnehmerStaging.setAustritt("invalid-date");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("austritt", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    void whenAustrittIsBeforeEintritt_thenValidationFails() {
        teilnehmerStaging.setEintritt("02.01.2023");
        teilnehmerStaging.setAustritt("01.01.2023");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertEquals(2, teilnehmer.getErrors().size());
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "eintritt".equals(error.getError())));
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "austritt".equals(error.getError())));
    }

}
