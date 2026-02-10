package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerDataStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeilnehmerDtoSVNValidationUnitTest {
    @Mock
    private GlobalUserHolder globalUserHolder;

    private TeilnehmerDtoSVNValidation validation;

    private static final LocalDate BIRTH_DATE = LocalDate.of(2004, 6, 17);
    private static final String TEST_USERNAME = "test.user";

    @BeforeEach
    public void setUp() {
        validation = new TeilnehmerDtoSVNValidation(globalUserHolder);
    }

    private TeilnehmerDto createTestTeilnehmerDto(String svn) {
        TeilnehmerDto teilnehmerDto = new TeilnehmerDto();
        teilnehmerDto.setSvNummer(svn);
        return teilnehmerDto;
    }

    private Teilnehmer createTestTeilnehmer() {
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setGeburtsdatum(BIRTH_DATE);
        teilnehmer.setErrors(new ArrayList<>());
        return teilnehmer;
    }

    @Test
    void validSocialSecurityNumber_returnsTrue() {
        String svn = "3154170604";
        TeilnehmerDto teilnehmerDto = createTestTeilnehmerDto(svn);
        Teilnehmer teilnehmer = createTestTeilnehmer();

        boolean result = validation.executeValidation(teilnehmerDto, teilnehmer);

        assertTrue(result);
        assertEquals(svn, teilnehmer.getSvNummer());
        verify(globalUserHolder, never()).getUsername();
    }

    @Test
    void null_returnsFalseAndAddsError() {
        when(globalUserHolder.getUsername()).thenReturn(TEST_USERNAME);
        TeilnehmerDto teilnehmerDto = createTestTeilnehmerDto(null);
        Teilnehmer teilnehmer = createTestTeilnehmer();

        boolean result = validation.executeValidation(teilnehmerDto, teilnehmer);

        assertFalse(result);
        assertNull(teilnehmer.getSvNummer());
        TeilnehmerDataStatus error = teilnehmer.getErrors().get(0);
        assertEquals("svNummer", error.getError());
        assertEquals("Das Feld ist leer", error.getCause());
        assertEquals(TEST_USERNAME, error.getCreatedBy());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void emptyOrWhitespace_returnsFalseAndAddsError(String svn) {
        when(globalUserHolder.getUsername()).thenReturn(TEST_USERNAME);
        TeilnehmerDto teilnehmerDto = createTestTeilnehmerDto(svn);
        Teilnehmer teilnehmer = createTestTeilnehmer();

        boolean result = validation.executeValidation(teilnehmerDto, teilnehmer);

        assertFalse(result);
        assertEquals(1, teilnehmer.getErrors().size());
        assertEquals("svNummer", teilnehmer.getErrors().get(0).getError());
        assertEquals("Das Feld ist leer", teilnehmer.getErrors().get(0).getCause());
    }
}
