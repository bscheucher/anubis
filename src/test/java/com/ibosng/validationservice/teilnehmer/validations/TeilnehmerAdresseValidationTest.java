package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.impl.LandServiceImpl;
import com.ibosng.dbservice.services.impl.PlzServiceImpl;
import com.ibosng.validationservice.teilnehmer.validations.imported.TeilnehmerAdresseValidation;
import com.ibosng.validationservice.utils.ValidationHelpers;
import com.ibosng.validationservice.validations.OrtValidation;
import com.ibosng.validationservice.validations.PLZValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class TeilnehmerAdresseValidationTest extends BaseIntegrationTest {
    @Mock
    private PlzServiceImpl plzService;
    @Mock
    private LandServiceImpl landService;
    @Mock
    private PLZValidation plzValidation;
    @Mock
    private OrtValidation ortValidation;
    @Mock
    private ValidationHelpers validationHelpers;

    private TeilnehmerAdresseValidation teilnehmerAdresseValidation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        teilnehmerAdresseValidation = new TeilnehmerAdresseValidation(plzService, plzValidation, ortValidation, landService, validationHelpers);
        Land land = new Land();
        when(landService.findByTelefonvorwahl(anyString())).thenReturn(Collections.singletonList(land));
        teilnehmerAdresseValidation.setSources(new HashSet<>(Collections.singleton(TeilnehmerSource.VHS)));
    }

    @Test
    public void testValidAdresse() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStrasse("Musterstraße");
        teilnehmerStaging.setPlz("1010");
        teilnehmerStaging.setOrt("Wien");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.getAdresse().setStatus(Status.ACTIVE);
        teilnehmer.setErrors(new ArrayList<>());

        Plz plZ = new Plz();
        plZ.setPlz(1010);

        when(plzService.findOrtByPlz(1010)).thenReturn(Collections.singletonList("Wien"));
        when(plzService.getAllPlz()).thenReturn(Collections.singletonList(1010));
        when(plzService.getAllOrt()).thenReturn(Collections.singletonList("Wien"));
        when(plzValidation.validatePlz("1010")).thenReturn(plZ);
        when(ortValidation.validateOrt("Wien")).thenReturn("Wien");

        boolean result = teilnehmerAdresseValidation.executeValidation(teilnehmerStaging, teilnehmer);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    public void testInvalidAdresse() {
        // Arrange
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStrasse("InvalidStreet12?3");
        teilnehmerStaging.setPlz("1010");
        teilnehmerStaging.setOrt("Wien");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.setErrors(new ArrayList<>());
        Plz plZ = new Plz();
        plZ.setPlz(1010);

        boolean result = teilnehmerAdresseValidation.executeValidation(teilnehmerStaging, teilnehmer);
        when(plzService.getAllPlz()).thenReturn(Collections.singletonList(1010));
        when(plzService.getAllOrt()).thenReturn(Collections.singletonList("Wien"));
        when(plzValidation.validatePlz("1010")).thenReturn(plZ);
        when(ortValidation.validateOrt("Wien")).thenReturn("Wien");

        assertFalse(result);
        assertFalse(teilnehmer.getErrors().isEmpty());
        assertEquals("strasse", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    public void testInvalidPlzOrtMismatch() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStrasse("Musterstraße");
        teilnehmerStaging.setPlz("1010");
        teilnehmerStaging.setOrt("Graz");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.getAdresse().setStatus(Status.ACTIVE);
        teilnehmer.setErrors(new ArrayList<>());
        Plz plZ = new Plz();
        plZ.setPlz(1010);

        when(plzService.findOrtByPlz(1010)).thenReturn(Collections.singletonList("Wien"));
        when(plzService.getAllPlz()).thenReturn(Collections.singletonList(1010));
        when(plzService.getAllOrt()).thenReturn(Collections.singletonList("Graz"));
        when(plzValidation.validatePlz("1010")).thenReturn(plZ);
        when(ortValidation.validateOrt("Graz")).thenReturn("Graz");

        boolean result = teilnehmerAdresseValidation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertFalse(teilnehmer.getErrors().isEmpty());
        assertEquals("plz", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    public void testEmptyStrasse() {
        // Arrange
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStrasse("");
        teilnehmerStaging.setPlz("1010");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        teilnehmerStaging.setOrt("Wien");
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.setErrors(new ArrayList<>());

        boolean result = teilnehmerAdresseValidation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertFalse(teilnehmer.getErrors().isEmpty());
        assertEquals("strasse", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    public void testEmptyPlz() {

        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStrasse("Musterstraße");
        teilnehmerStaging.setPlz("");
        teilnehmerStaging.setOrt("Wien");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.getAdresse().setStatus(Status.ACTIVE);
        teilnehmer.setErrors(new ArrayList<>());

        boolean result = teilnehmerAdresseValidation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertFalse(teilnehmer.getErrors().isEmpty());
        assertEquals("plz", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    public void testNullStrasse() {
        // Arrange
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setStrasse(null);
        teilnehmerStaging.setPlz("1010");
        teilnehmerStaging.setOrt("Wien");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.setErrors(new ArrayList<>());

        boolean result = teilnehmerAdresseValidation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertFalse(teilnehmer.getErrors().isEmpty());
        assertEquals("strasse", teilnehmer.getErrors().get(0).getError());
    }

    @Test
    public void testInvalidTitiel() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();

        teilnehmerStaging.setTitel("test");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS);
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.setErrors(new ArrayList<>());

        boolean result = teilnehmerAdresseValidation.executeValidation(teilnehmerStaging, teilnehmer);

        assertFalse(result);
        assertFalse(teilnehmer.getErrors().isEmpty());
    }

}