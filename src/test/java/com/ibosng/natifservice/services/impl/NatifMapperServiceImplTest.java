package com.ibosng.natifservice.services.impl;


import com.ibosng.dbservice.entities.natif.Kompetenz;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.natif.KompetenzenService;
import com.ibosng.natifservice.dtos.extractions.BboxRefsDto;
import com.ibosng.natifservice.dtos.extractions.DetailsDto;
import com.ibosng.natifservice.dtos.extractions.ExtractionsDto;
import com.ibosng.natifservice.dtos.extractions.kompetenzen.FachlichDto;
import com.ibosng.natifservice.dtos.extractions.kompetenzen.UeberfachlichDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NatifMapperServiceImplTest {


    @Mock
    private KompetenzenService kompetenzenService;

    @Mock
    private TeilnehmerService teilnehmerService;

    @InjectMocks
    private NatifMapperServiceImpl natifMapperServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testKompetenzMapping() {
        Integer teilnehmerId = 1;
        Teilnehmer mockTeilnehmer = new Teilnehmer();
        mockTeilnehmer.setId(teilnehmerId);
        when(teilnehmerService.findById(teilnehmerId)).thenReturn(Optional.of(mockTeilnehmer));
        when(kompetenzenService.isExists(any())).thenReturn(false);

        ExtractionsDto extractionsDto = testData();

        natifMapperServiceImpl.saveKompetenz(extractionsDto, teilnehmerId);

        // Expecting two calls: one for fachlich and one for ueberfachlich
        ArgumentCaptor<Kompetenz> kompetenzCaptor = ArgumentCaptor.forClass(Kompetenz.class);
        verify(kompetenzenService, times(2)).save(kompetenzCaptor.capture());

        List<Kompetenz> capturedKompetenzen = kompetenzCaptor.getAllValues();

        Kompetenz fachlichKompetenz = capturedKompetenzen.get(0);
        assertEquals("fachlich", fachlichKompetenz.getArt());
        assertEquals("Sonnenstudiomitarbeiter", fachlichKompetenz.getName());
        assertEquals(0.51231, fachlichKompetenz.getConfidenceName());
        assertEquals(new BigDecimal(100), fachlichKompetenz.getScore());
        assertEquals(0.74123, fachlichKompetenz.getConfidenceScore());
        assertEquals(teilnehmerId, fachlichKompetenz.getTeilnehmer().getId());

        Kompetenz ueberfachlichKompetenz = capturedKompetenzen.get(1);
        assertEquals("ueberfachlich", ueberfachlichKompetenz.getArt());
        assertEquals("Teamplayer", ueberfachlichKompetenz.getName());
        assertEquals(0.61234, ueberfachlichKompetenz.getConfidenceName());
        assertEquals(new BigDecimal(80), ueberfachlichKompetenz.getScore());
        assertEquals(0.83145, ueberfachlichKompetenz.getConfidenceScore());
        assertEquals(teilnehmerId, ueberfachlichKompetenz.getTeilnehmer().getId());
    }


    private ExtractionsDto testData() {
        ExtractionsDto extractionsDto = new ExtractionsDto();
        extractionsDto.setSchemaVersion(1);
        extractionsDto.setDocumentType("custom_extraction");
        extractionsDto.setKostentraeger("BeispielKostentraeger");
        extractionsDto.setNachname("Mustermann");
        extractionsDto.setVorname("Max");
        extractionsDto.setSvNr(123456789);

        FachlichDto fachlichDto = new FachlichDto();
        BboxRefsDto bboxRefsDto = new BboxRefsDto();
        bboxRefsDto.setPageNumber(1);
        bboxRefsDto.setBboxId(184);
        fachlichDto.setKompetenz(new DetailsDto(List.of(bboxRefsDto), "Sonnenstudiomitarbeiter",
                false, "", 0.51231));
        fachlichDto.setScore(new DetailsDto(List.of(bboxRefsDto), "100",
                false, "", 0.74123));
        extractionsDto.setFachlich(List.of(fachlichDto));

        UeberfachlichDto ueberfachlichDto = new UeberfachlichDto();
        ueberfachlichDto.setKompetenz(new DetailsDto(List.of(bboxRefsDto), "Teamplayer",
                false, "", 0.61234));
        ueberfachlichDto.setScore(new DetailsDto(List.of(bboxRefsDto), "80",
                false, "", 0.83145));
        extractionsDto.setUeberfachlich(List.of(ueberfachlichDto));

        extractionsDto.setInteressengebiete(List.of());
        extractionsDto.setSuchbegriffe(List.of());
        extractionsDto.setTeilDerBerufsobergruppe(List.of());
        extractionsDto.setVoraussetzung(List.of());
        extractionsDto.setZertifikate(List.of());
        extractionsDto.setAmsSechsSteller(List.of());

        return extractionsDto;
    }

}