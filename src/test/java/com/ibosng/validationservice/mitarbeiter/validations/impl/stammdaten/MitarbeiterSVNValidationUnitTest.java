package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.StammdatenDataStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MitarbeiterSVNValidationUnitTest {

    @Mock
    private GlobalUserHolder globalUserHolder;

    private MitarbeiterSVNValidation validation;
    private static final LocalDate BIRTH_DATE = LocalDate.of(2001, 11, 7);
    private static final String TEST_USERNAME = "test.user";

    @BeforeEach
    void setUp() {
        validation = new MitarbeiterSVNValidation(globalUserHolder);
    }

    private StammdatenDto createTestDto(String svn) {
        StammdatenDto dto = new StammdatenDto();
        dto.setSvnr(svn);
        return dto;
    }

    private Stammdaten createTestStammdaten() {
        Stammdaten stammdaten = new Stammdaten();
        stammdaten.setGeburtsdatum(BIRTH_DATE);
        return stammdaten;
    }

    @Test
    void validSocialSecurityNumber_returnsTrue() {
        var svn = "7968071101";
        StammdatenDto dto = createTestDto(svn);
        Stammdaten stammdaten = createTestStammdaten();

        boolean result = validation.executeValidation(dto, stammdaten);

        assertTrue(result);
        assertEquals(svn, stammdaten.getSvnr());
        verify(globalUserHolder, never()).getUsername();
    }

    @Test
    void null_returnsFalseAndAddsError() {
        when(globalUserHolder.getUsername()).thenReturn(TEST_USERNAME);
        StammdatenDto dto = createTestDto(null);
        Stammdaten stammdaten = createTestStammdaten();

        boolean result = validation.executeValidation(dto, stammdaten);

        assertFalse(result);
        assertNull(stammdaten.getSvnr());
        StammdatenDataStatus error = stammdaten.getErrors().get(0);
        assertEquals("svnr", error.getError());
        assertEquals("Das Feld ist leer", error.getCause());
        assertEquals(TEST_USERNAME, error.getCreatedBy());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void emptyOrWhiteSpace_returnsFalseAndAddsError(String svn) {
        when(globalUserHolder.getUsername()).thenReturn(TEST_USERNAME);
        StammdatenDto dto = createTestDto(svn);
        Stammdaten stammdaten = createTestStammdaten();

        boolean result = validation.executeValidation(dto, stammdaten);

        assertFalse(result);
        assertNull(stammdaten.getSvnr());
        StammdatenDataStatus error = stammdaten.getErrors().get(0);
        assertEquals("svnr", error.getError());
        assertEquals("Das Feld ist leer", error.getCause());
        assertEquals(TEST_USERNAME, error.getCreatedBy());
    }
}