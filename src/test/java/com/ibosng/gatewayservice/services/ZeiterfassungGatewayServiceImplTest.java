package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbmapperservice.services.ZeitbuchungenMapperService;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.dbservice.services.zeiterfassung.AuszahlungsantragService;
import com.ibosng.dbservice.services.zeiterfassung.ZeitspeicherService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.impl.ZeiterfassungGatewayServiceImpl;
import com.ibosng.gatewayservice.utils.Helpers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ZeiterfassungGatewayServiceImplTest extends BaseIntegrationTest {

    @InjectMocks
    private ZeiterfassungGatewayServiceImpl zeiterfassungGatewayService;

    @Mock
    private AbwesenheitService abwesenheitService;

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    private LeistungserfassungService leistungserfassungService;

    @Mock
    private ZeitbuchungService zeitbuchungService;

    @Mock
    private ZeitspeicherService zeitspeicherService;

    @Mock
    private Gateway2Validation gateway2Validation;

    @Mock
    private StammdatenService stammdatenService;

    @Mock
    private ZeitbuchungenMapperService zeitbuchungenMapper;

    @Mock
    private PersonalnummerService personalnummerService;

    @Mock
    private Helpers helpers;

    @Mock
    private AuszahlungsantragService auszahlungsantragService;

    @Mock
    private ZeitausgleichService zeitausgleichService;


    @Test
    void testGetAbwesenheit() {
        Integer abwesenheitId = 1;

        Abwesenheit mockedAbwesenheit = new Abwesenheit();
        mockedAbwesenheit.setId(abwesenheitId);
        when(abwesenheitService.findById(anyInt())).thenReturn(Optional.of(mockedAbwesenheit));
        when(abwesenheitService.mapToAbwesenheitDto(any())).thenReturn(AbwesenheitDto.builder().id(1).build());
        PayloadResponse response = zeiterfassungGatewayService.getAbwesenheit(abwesenheitId);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(PayloadTypes.ABWESENHEIT.getValue(), response.getData().get(0).getType());
    }

    @Test
    @Disabled
    void testDeleteAbwesenheit() {
        Integer abwesenheitId = 123;

        PayloadResponse response = zeiterfassungGatewayService.deleteAbwesenheit(abwesenheitId);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.isSuccess(), "Response should indicate success");
    }

    @Disabled
    @Test
    void testGetAbwesenheitenList() {
        String authorizationToken = "Bearer Authorization";
        Abwesenheit mockedResult = new Abwesenheit();
        mockedResult.setId(1);
        mockedResult.setPersonalnummer(createPersonalNummer());
        List<Abwesenheit> mockedAbwesenenheiten = new ArrayList<>();
        mockedAbwesenenheiten.add(mockedResult);

        Benutzer benutzer = new Benutzer();
        benutzer.setFirstName("John");
        benutzer.setLastName("Doe");
        benutzer.setCreatedBy("Max");
        benutzer.setEmail("email@test.com");
        benutzer.setId(1);
        String sortProperty = "startDate";
        String sortDirection = "ASC";
        int page = 0;
        int size = 100;

        when(abwesenheitService.findAllByFuehrungskraefteId(anyInt())).thenReturn(mockedAbwesenenheiten);
        when(benutzerDetailsService.getUserFromToken(authorizationToken)).thenReturn(benutzer);
        PayloadResponse response = zeiterfassungGatewayService.getAbwesenheitenList(authorizationToken, true, "", null, sortProperty, sortDirection, page, size);

        assertNotNull(response);
        assertTrue(response.isSuccess(), "Response should indicate success");
    }


    private Personalnummer createPersonalNummer() {
        Personalnummer personalnummer = new Personalnummer();
        personalnummer.setId(1);
        personalnummer.setPersonalnummer("123456");

        return personalnummer;
    }

}