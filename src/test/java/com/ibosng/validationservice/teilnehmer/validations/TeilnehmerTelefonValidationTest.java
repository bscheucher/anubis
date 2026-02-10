package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.services.impl.LandServiceImpl;
import com.ibosng.dbservice.services.impl.TelefonServiceImpl;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerTelefonValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TeilnehmerTelefonValidationTest extends BaseIntegrationTest {

    private static final String LAND = "Österreich";
    private static final String LAND_CODE = "AT";
    private static final String LAND_VORWAHL = "+43";
    private static final String TEST_USER = "testUser";

    @Mock
    private LandServiceImpl landService;

    @Mock
    private TelefonServiceImpl telefonService;

    private Teilnehmer teilnehmer;

    private Validation<TeilnehmerStaging, Teilnehmer> validation;

    private Land land;
    private Telefon telefon;


    @BeforeEach
    public void setup() {
        telefon = new Telefon();
        MockitoAnnotations.initMocks(this);
        teilnehmer = new Teilnehmer();
        validation = new TeilnehmerTelefonValidation(landService, telefonService);
        land = new Land(LAND, LAND_CODE, LAND_VORWAHL, TEST_USER);
        when(landService.findByTelefonvorwahl(anyString())).thenReturn(Collections.singletonList(land));
    }

    @Test
    void testValidSinglePhoneNumber() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSource(TeilnehmerSource.VHS);
        staging.setTelefon("+43123456789");
        when(telefonService.save(any())).thenReturn(telefon);

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertTrue(result);
        assertEquals(1, teilnehmer.getTelefons().size());
    }

    @Test
    void testInvalidSinglePhoneNumber() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSource(TeilnehmerSource.VHS);
        staging.setTelefon("+4377013243");

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertFalse(result);
        assertEquals(0, teilnehmer.getTelefons().size());
    }

    @Test
    void testValidMultiplePhoneNumbersWithoutLabels() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSource(TeilnehmerSource.VHS);
        staging.setTelefon("0676/1234567, 0660/7654321");
        when(telefonService.save(any())).thenReturn(telefon);

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertTrue(result);
        assertEquals(2, teilnehmer.getTelefons().size());
    }

    @Test
    void testInvalidMultiplePhoneNumbersWithoutLabelsFirstValid() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSource(TeilnehmerSource.VHS);
        staging.setTelefon("0660/7654321, .*/12*fg34567");
        when(telefonService.save(any())).thenReturn(telefon);

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getTelefons().size());
    }

    @Test
    void testInvalidMultiplePhoneNumbersWithoutLabelsSecondValid() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSource(TeilnehmerSource.VHS);
        staging.setTelefon(".*/12*fg34567, 0660/7654321");

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertFalse(result);
        assertEquals(0, teilnehmer.getTelefons().size());
    }

    @Test
    void testValidMultiplePhoneNumbersWithLabels() {
        TeilnehmerStaging staging = new TeilnehmerStaging();
        staging.setSource(TeilnehmerSource.VHS);
        staging.setTelefon("0676/1234567 (eigene), 0660/7654321 (Gatte)");
        when(telefonService.save(any())).thenReturn(telefon);

        boolean result = validation.executeValidation(staging, teilnehmer);

        assertTrue(result);
        assertEquals(2, teilnehmer.getTelefons().size());
    }

}
