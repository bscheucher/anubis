package com.ibosng.validationservice.teilnehmer.validations;


import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.impl.RgsServiceImpl;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.RgsValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RgsValidationTest extends BaseIntegrationTest {

    @Mock
    private RgsServiceImpl rgsService;

    @Mock
    private Teilnehmer teilnehmer;

    private Teilnehmer2Seminar teilnehmer2Seminar;

    private Validation<TeilnehmerStaging, Teilnehmer2Seminar> validation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(teilnehmer.getErrors()).thenReturn(new ArrayList<>());
        validation = new RgsValidation(rgsService);
        teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());
        teilnehmer2Seminar = new Teilnehmer2Seminar();
        teilnehmer2Seminar.setTeilnehmer(teilnehmer);
    }

    @Test
    public void testValidRgs() {
        List<Integer> allRgs = Arrays.asList(123, 456, 789);
        when(rgsService.getAllRgs()).thenReturn(allRgs);

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setRgs("123");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    public void testInvalidRgsFormat() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setRgs("ABC");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "rgs".equals(error.getError())));
    }

    @Test
    public void testInvalidRgsNotExists() {
        List<Integer> allRgs = Arrays.asList(123, 456, 789);
        when(rgsService.getAllRgs()).thenReturn(allRgs);

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setRgs("999");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "rgs".equals(error.getError())));
    }

}