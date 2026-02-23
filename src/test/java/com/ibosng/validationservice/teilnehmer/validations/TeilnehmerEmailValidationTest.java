package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerEmailValidation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeilnehmerEmailValidationTest extends BaseIntegrationTest {

    @Test
    public void testEmailValidation_validEmail() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setEmail("test@example.com");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmer.setErrors(new ArrayList<>());
        TeilnehmerEmailValidation validation = new TeilnehmerEmailValidation();
        assertTrue(validation.executeValidation(teilnehmerStaging, teilnehmer));
    }

    @Test
    public void testEmailValidation_invalidEmail() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmerStaging.setEmail("invalid-email");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmer.setErrors(new ArrayList<>());
        TeilnehmerEmailValidation validation = new TeilnehmerEmailValidation();
        assertFalse(validation.executeValidation(teilnehmerStaging, teilnehmer));
    }

}
