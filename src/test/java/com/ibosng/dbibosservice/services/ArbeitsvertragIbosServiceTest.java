package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.dtos.ContractDto;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragIbosRepository;
import com.ibosng.dbibosservice.services.impl.mitarbeiter.ArbeitsvertragIbosServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ArbeitsvertragIbosServiceTest extends BaseIntegrationTest {

    private static final String VERWENDUNGSGRUPPE = "verwendungsgruppe";
    private static final String STUFE = "stufe";
    private static final String FUNCTION = "function";
    private static final long KST = 1L;
    private static final String PERSONAL_NUMMER = "personalNummer";
    private static final String ARBEITSZEITMODEL = "arbeitszeitmodel";
    private static final String DIENSTORT = "dienstort";
    private static final String NICHT_LEISTUNGEN = "nichtLeistungen";
    private static final BigDecimal WOCHENSTUNDEN = BigDecimal.valueOf(40);


    @InjectMocks
    ArbeitsvertragIbosServiceImpl contractService;

    @Mock
    ArbeitsvertragIbosRepository arbeitsvertragIbosRepository;

    @Test
    public void getContractsTest() {
        LocalDate from = LocalDate.now();
        Date eintrittsdatum = Date.valueOf(from.minusDays(5L));

        when(arbeitsvertragIbosRepository.getContractsDataRaw(anyString()))
                .thenReturn(Collections.singletonList(createRawData(from, eintrittsdatum)));

        List<ContractDto> contracts = contractService.getContracts("userName");

        assertFalse(contracts.isEmpty());
        assertEquals(1, contracts.size());

        assertEquals(VERWENDUNGSGRUPPE, contracts.get(0).getVerwendungsgruppe());
        assertEquals(STUFE, contracts.get(0).getStufe());
        assertEquals(PERSONAL_NUMMER, contracts.get(0).getPersonalNummer());
        assertNotNull(contracts.get(0).getEintrittsdatum());
        assertEquals(from.minusDays(5L), contracts.get(0).getEintrittsdatum());
    }

    @Test
    public void getContractsWithoutLeistungen() {
        LocalDate from = LocalDate.now().minusDays(1L);

        when(arbeitsvertragIbosRepository.getContractsWithoutLeistungen(anyString()))
                .thenReturn(Collections.singletonList(createRawData(from, null)));

        List<ContractDto> contracts = contractService.getContractsWithoutLeistungen("userName");

        assertFalse(contracts.isEmpty());
        assertEquals(1, contracts.size());

        assertEquals(VERWENDUNGSGRUPPE, contracts.get(0).getVerwendungsgruppe());
        assertEquals(STUFE, contracts.get(0).getStufe());
        assertEquals(PERSONAL_NUMMER, contracts.get(0).getPersonalNummer());
        assertNull(contracts.get(0).getEintrittsdatum());
    }

    @Test
    public void getAllContracts() {
        LocalDate from = LocalDate.now();
        Date eintrittsdatum = Date.valueOf(from.minusDays(5L));

        when(arbeitsvertragIbosRepository.getContractsDataRaw(anyString()))
                .thenReturn(Collections.singletonList(createRawData(from, eintrittsdatum)));
        when(arbeitsvertragIbosRepository.getContractsWithoutLeistungen(anyString()))
                .thenReturn(Collections.singletonList(createRawData(from.plusDays(1L), null)));

        List<ContractDto> contracts = contractService.getAllContracts("userName");

        assertFalse(contracts.isEmpty());
        assertEquals(2, contracts.size());

        ContractDto contractDto1 = contracts.get(0);
        assertEquals(VERWENDUNGSGRUPPE, contractDto1.getVerwendungsgruppe());
        assertEquals(STUFE, contractDto1.getStufe());
        assertEquals(PERSONAL_NUMMER, contractDto1.getPersonalNummer());
        assertNotNull(contractDto1.getEintrittsdatum());
        assertEquals(from.minusDays(5L), contractDto1.getEintrittsdatum());

        ContractDto contractDto2 = contracts.get(1);
        assertEquals(VERWENDUNGSGRUPPE, contractDto2.getVerwendungsgruppe());
        assertEquals(STUFE, contractDto2.getStufe());
        assertEquals(PERSONAL_NUMMER, contractDto2.getPersonalNummer());
        assertNull(contractDto2.getEintrittsdatum());

        assertTrue(contractDto2.getDatumVon().isAfter(contractDto1.getDatumVon()));
    }

    private Object[] createRawData(LocalDate from, Date eintrittsdatum) {
        return new Object[]{
                Date.valueOf(from),
                Date.valueOf(from.plusDays(30L)),
                VERWENDUNGSGRUPPE,
                STUFE,
                FUNCTION,
                KST,
                PERSONAL_NUMMER,
                eintrittsdatum,
                WOCHENSTUNDEN,
                ARBEITSZEITMODEL,
                DIENSTORT,
                NICHT_LEISTUNGEN
        };
    }
}
