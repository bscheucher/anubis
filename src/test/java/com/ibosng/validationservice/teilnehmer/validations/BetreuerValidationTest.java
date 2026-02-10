package com.ibosng.validationservice.teilnehmer.validations;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.betreuer.Betreuer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.services.impl.BetreuerServiceImpl;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.teilnehmer.validations.imported.BetreuerValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BetreuerValidationTest extends BaseIntegrationTest {

    @Mock
    private Teilnehmer teilnehmer;

    @Mock
    private Teilnehmer2Seminar teilnehmer2Seminar;

    @Mock
    private Betreuer betreuer;

    @Mock
    private BetreuerServiceImpl betreuerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        betreuer = new Betreuer();
        when(betreuerService.save(any(Betreuer.class))).thenReturn(betreuer);
        when(teilnehmer2Seminar.getBetreuer()).thenReturn(betreuer);
        when(teilnehmer.getErrors()).thenReturn(new ArrayList<>());
        when(teilnehmer2Seminar.getTeilnehmer()).thenReturn(teilnehmer);
    }


    @Test
    public void testValidBetreuer() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setBetreuerVorname("Max");
        teilnehmerStaging.setBetreuerNachname("Mustermann");
        teilnehmerStaging.setBetreuerTitel("Dipl_Ing");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS_EAMS);

        Validation<TeilnehmerStaging, Teilnehmer2Seminar> validation = new BetreuerValidation(betreuerService);
        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }

    @Test
    public void testInvalidBetreuerVorname() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setBetreuerVorname("Max1"); // Invalid name with a digit
        teilnehmerStaging.setBetreuerNachname("Mustermann");
        teilnehmerStaging.setBetreuerTitel("Dr");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS_EAMS);
        Teilnehmer2Seminar teilnehmer2Seminar = new Teilnehmer2Seminar();
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());
        teilnehmer2Seminar.setTeilnehmer(teilnehmer);
        Validation<TeilnehmerStaging, Teilnehmer2Seminar> validation = new BetreuerValidation(betreuerService);
        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "betreuerVorname".equals(error.getError())));
    }

    @Test
    public void testInvalidBetreuerNachname() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setBetreuerVorname("Max");
        teilnehmerStaging.setBetreuerNachname("Mustermann123"); // Invalid surname with digits
        teilnehmerStaging.setBetreuerTitel("Dr");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS_EAMS);
        Teilnehmer2Seminar teilnehmer2Seminar = new Teilnehmer2Seminar();
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setErrors(new ArrayList<>());
        teilnehmer2Seminar.setTeilnehmer(teilnehmer);
        Validation<TeilnehmerStaging, Teilnehmer2Seminar> validation = new BetreuerValidation(betreuerService);
        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);
        assertFalse(result);
        assertTrue(teilnehmer.getErrors().stream()
                .anyMatch(error -> "betreuerNachname".equals(error.getError())));
    }


/*    @Test
    public void testInvalidBetreuerTitel() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setBetreuerVorname("Max");
        teilnehmerStaging.setBetreuerNachname("Mustermann");
        teilnehmerStaging.setBetreuerTitel("InvalidTitle"); // Invalid title
        teilnehmerStaging.setSource(TeilnehmerSource.VHS_EAMS);

        Validation<TeilnehmerStaging, Teilnehmer2Seminar> validation = new BetreuerValidation(betreuerService);
        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertFalse(result);
        assertTrue(teilnehmer.getDataStatuses().contains(TeilnehmerDataStatus.INVALID_BETREUER_TITEL));
    }*/

    @Test
    public void testMultipleBetreuerTitel() {
        TeilnehmerStaging teilnehmerStaging = new TeilnehmerStaging();
        teilnehmerStaging.setBetreuerVorname("Max");
        teilnehmerStaging.setBetreuerNachname("Mustermann");
        teilnehmerStaging.setBetreuerTitel("M.A., B.A.");
        teilnehmerStaging.setSource(TeilnehmerSource.VHS_EAMS);

        Validation<TeilnehmerStaging, Teilnehmer2Seminar> validation = new BetreuerValidation(betreuerService);
        boolean result = validation.executeValidation(teilnehmerStaging, teilnehmer2Seminar);

        assertTrue(result);
        assertTrue(teilnehmer.getErrors().isEmpty());
    }
}
