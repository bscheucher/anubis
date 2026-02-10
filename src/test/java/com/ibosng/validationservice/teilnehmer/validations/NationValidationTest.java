package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.NationValidation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class NationValidationTest extends BaseIntegrationTest {

    @Mock
    private LandService landService;


    @Test
    void testExecuteValidation_validNation() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setNation("AT"); // Set with the valid nation
        teilnehmer.setErrors(new ArrayList<>());
        Land land = new Land();
        when(landService.findByEldaCode(any())).thenReturn(land);
        when(landService.getLandFromCountryCode(any())).thenReturn(land);
        Validation<TeilnehmerStaging, Teilnehmer> validation = new NationValidation(landService);

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    void testExecuteValidation_emptyNation() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());
        when(landService.findByEldaCode(any())).thenReturn(null);
        when(landService.getLandFromCountryCode(any())).thenReturn(null);
        Validation<TeilnehmerStaging, Teilnehmer> validation = new NationValidation(landService);

        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

}
