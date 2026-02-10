package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.imported.ZubuchungValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ZubuchungValidationTest extends BaseIntegrationTest {

    private ZubuchungValidation validation;
    private TeilnehmerStaging teilnehmerStaging;
    private Teilnehmer teilnehmer;
    private Teilnehmer2Seminar teilnehmer2Seminar;

    @BeforeEach
    void setUp() {
        validation = new ZubuchungValidation();
        teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());
        teilnehmer2Seminar = new Teilnehmer2Seminar();
        teilnehmer2Seminar.setTeilnehmer(teilnehmer);
    }


    @Test
    void whenZubuchungIsValid_thenValidationSucceeds() {
        teilnehmerStaging.setZubuchung("02.01.2023");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void whenZubuchungIsInvalid_thenValidationFails() {
        teilnehmerStaging.setZubuchung("invalid-date");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("zubuchung", teilnehmer.getErrors().get(0).getError());
    }

}
