package com.ibosng.validationservice.services.impl;


import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.impl.TeilnehmerServiceImpl;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.dbservice.services.impl.ValidationsServiceImpl;
import com.ibosng.validationservice.AbstractValidator;
import com.ibosng.validationservice.teilnehmer.ImportedTeilnehmerValidator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorFactoryImplTest extends BaseIntegrationTest {

    @Mock
    private TeilnehmerServiceImpl teilnehmerService;

    @Mock
    private TeilnehmerStagingServiceImpl teilnehmerStagingService;

    @Mock
    private ValidationsServiceImpl validationsService;

    @InjectMocks
    private ValidatorFactoryImpl validatorFactoryImpl;

    @Test
    @Disabled
    void testCreateValidator_TeilnehmerType() {
        String identifier = "some-identifier";
        AbstractValidator<TeilnehmerStaging, Teilnehmer> validator = validatorFactoryImpl.createValidator(identifier, TeilnehmerStaging.class, Teilnehmer.class);

        assertNotNull(validator, "Validator should not be null");
        assertTrue(validator instanceof ImportedTeilnehmerValidator, "Validator should be instance of TeilnehmerValidator");
        assertEquals(identifier, validator.getIdentifier(), "Identifier should match");
    }

    @Test
    void testCreateValidator_UnsupportedType() {
        String identifier = "some-identifier";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validatorFactoryImpl.createValidator(identifier, UnsupportedType.class, UnsupportedType.class)
        );

        assertEquals("Unsupported type: " + UnsupportedType.class, exception.getMessage());
    }

    private static class UnsupportedType {
    }
}
