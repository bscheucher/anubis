package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.repositories.SeminarIbosRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class SeminarIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private SeminarIbosRepository seminarIbosRepository;

    @Resource
    private SeminarIbosService seminarService;

    @BeforeEach
    void setUp() {
        SeminarIbos seminarIbos = createSeminarIbos(123, 456);
        seminarService.save(seminarIbos);
    }

    @Test
    void findAll() {
        List<SeminarIbos> result = seminarService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        SeminarIbos newSeminarIbos = createSeminarIbos(123, 456);
        SeminarIbos saved = seminarIbosRepository.save(newSeminarIbos);

        Optional<SeminarIbos> result = seminarService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    void save() {
        SeminarIbos seminarIbos = createSeminarIbos(789, 1011);

        SeminarIbos saved = seminarService.save(seminarIbos);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void saveAll() {
        SeminarIbos seminarIbos1 = createSeminarIbos(1213, 1415);
        SeminarIbos seminarIbos2 = createSeminarIbos(1617, 1819);

        List<SeminarIbos> savedList = seminarService.saveAll(Arrays.asList(seminarIbos1, seminarIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        SeminarIbos seminarIbos = createSeminarIbos(2223, 2021);
        SeminarIbos saved = seminarIbosRepository.save(seminarIbos);

        Integer id = saved.getId();
        seminarService.deleteById(id);

        Optional<SeminarIbos> result = seminarIbosRepository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    public void findBySeminarDetails() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate dateFrom = LocalDate.parse("25.09.2023", dateFormatter);
        LocalDate dateUntil = LocalDate.parse("12.01.2024", dateFormatter);
        LocalTime time = LocalTime.parse("08:00", timeFormatter);
        SeminarIbos seminarIbos = createSeminarIbos(123, 456);
        seminarIbos.setBezeichnung1("Deutsch_MAF_Standard_B2_K_43");
        seminarIbos.setDatumVon(dateFrom);
        seminarIbos.setDatumBis(dateUntil);
        seminarIbos.setZeitVon(time);
        seminarIbosRepository.save(seminarIbos);

        String[] seminarIdentifiers = "B2 Standard".split(" ");
        String seminarIdentifier = "K_43";
        Set<String> identifiers = new HashSet<>(Arrays.asList(seminarIdentifiers));
        identifiers.add(seminarIdentifier);
        List<SeminarIbos> foundSeminar = seminarService.findSeminarByDetails(identifiers, dateFrom, dateUntil, time);
        assertNotNull(foundSeminar);
    }

    private SeminarIbos createSeminarIbos(Integer pjnr, Integer mbbeSeminartyp) {
        SeminarIbos seminarIbos = new SeminarIbos();
        seminarIbos.setEruser("DB_SYNC");
        seminarIbos.setPjnr(pjnr);
        seminarIbos.setMbbeSeminartyp(mbbeSeminartyp);
        seminarIbos.setErda(LocalDateTime.now());
        return seminarIbos;
    }

}
