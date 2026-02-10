package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.impl.PlzServiceImpl;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerOrtValidation;
import com.ibosng.validationservice.validations.OrtValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Disabled
public class TeilnehmerOrtValidationTest extends BaseIntegrationTest {

    @Mock
    private PlzServiceImpl plzService;

    @Mock
    private OrtValidation ortValidation;

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
        validation = new TeilnehmerOrtValidation(ortValidation);
    }

    @Test
    public void testValidOrt() {
        List<String> allOrt = Arrays.asList("Wien", "Graz", "Linz");
        when(plzService.getAllOrt()).thenReturn(allOrt);

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmerStaging.setOrt("Wien");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    public void testInvalidOrtFormat() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setOrt("123");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "ort".equals(error.getError())));
    }

    @Test
    public void testInvalidOrtNotExists() {
        List<String> allOrt = Arrays.asList("Wien", "Graz", "Linz");
        when(plzService.getAllOrt()).thenReturn(allOrt);

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmerStaging.setOrt("Salzburg");

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "ort".equals(error.getError())));
    }

}