package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.impl.PlzServiceImpl;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerPlzValidation;
import com.ibosng.validationservice.validations.PLZValidation;
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

public class TeilnehmerPlzValidationTest extends BaseIntegrationTest {

    @Mock
    private PlzServiceImpl plzService;
    @Mock
    private PLZValidation plzValidation;

    @Mock
    private Teilnehmer teilnehmer;

    @Mock
    private Adresse adresse;

    private Validation<TeilnehmerStaging, Teilnehmer> validation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(teilnehmer.getErrors()).thenReturn(new ArrayList<>());
        when(teilnehmer.getAdresse()).thenReturn(adresse);
        validation = new TeilnehmerPlzValidation(plzValidation);
    }

    @Test
    public void testValidPlz() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmerStaging.setPlz("1010");
        Plz plz1 = new Plz();
        plz1.setPlz(1010);
        when(plzValidation.validatePlz("1010")).thenReturn(plz1);
        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    public void testInvalidPlzFormat() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setPlz("ABCD");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "plz".equals(error.getError())));
    }

    @Test
    public void testInvalidPlzNotExists() {
        List<Integer> allPlz = Arrays.asList(1010, 1020, 1030);
        when(plzService.getAllPlz()).thenReturn(allPlz);

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setPlz("9999");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "plz".equals(error.getError())));
    }

    private List<Plz> createPlz() {
        Plz plz1 = new Plz();
        Plz plz2 = new Plz();
        Plz plz3 = new Plz();
        plz1.setPlz(1010);
        plz2.setPlz(1020);
        plz3.setPlz(1030);
        return Arrays.asList(plz1, plz2, plz3);
    }

}