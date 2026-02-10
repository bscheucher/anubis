package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.dtos.SeminarDto;
import com.ibosng.dbibosservice.repositories.SeminarIbosRepository;
import com.ibosng.dbibosservice.services.impl.SeminarIbosServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SeminarIbosServiceMockTest extends BaseIntegrationTest {

    private static final Integer PROJEKTNUMMER = 123;
    private static final Integer SEMINARNUMMER = 456;
    private static final String ORT = "Wien";
    private static final String SEMINAR = "Seminar";

    @InjectMocks
    SeminarIbosServiceImpl seminarService;

    @Mock
    SeminarIbosRepository seminarIbosRepository;

    @Test
    public void getSeminarDataRawTest() {
        LocalDate von = LocalDate.now();
        int plusMonths = 3;
        LocalTime uhrzeitVon = LocalTime.now();
        int plusHours = 5;

        when(seminarIbosRepository.getSeminarDataRaw(anyString()))
                .thenReturn(Collections.singletonList(createRawData(von, plusMonths, uhrzeitVon, plusHours)));

        List<SeminarDto> seminarDtos = seminarService.getSeminarDataRaw("userName");

        assertFalse(seminarDtos.isEmpty());
        assertEquals(1, seminarDtos.size());

        String uhrzeit = uhrzeitVon.truncatedTo(ChronoUnit.SECONDS) + " - " + uhrzeitVon.plusHours(plusHours).truncatedTo(ChronoUnit.SECONDS);
        assertEquals(PROJEKTNUMMER, seminarDtos.get(0).getProjektNummer());
        assertEquals(SEMINARNUMMER, seminarDtos.get(0).getSeminarNummer());
        assertEquals(ORT, seminarDtos.get(0).getOrt());
        assertEquals(SEMINAR, seminarDtos.get(0).getSeminar());
        assertEquals(uhrzeit, seminarDtos.get(0).getUhrzeit());
        assertEquals(von, seminarDtos.get(0).getVon());
        assertEquals(von.plusMonths(plusMonths), seminarDtos.get(0).getBis());
    }


    private Object[] createRawData(LocalDate von, Integer plusMonths, LocalTime uhrzeitVon, Integer plusHours) {
        return new Object[]{
                PROJEKTNUMMER,
                SEMINARNUMMER,
                ORT,
                SEMINAR,
                Date.valueOf(von),
                Date.valueOf(von.plusMonths(plusMonths)),
                Time.valueOf(LocalTime.from(uhrzeitVon)),
                Time.valueOf(LocalTime.from(uhrzeitVon).plusHours(plusHours))
        };
    }
}
