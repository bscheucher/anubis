package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbibosservice.entities.smtn.SmTnId;
import com.ibosng.dbibosservice.repositories.SeminarTeilnehmerRepository;
import com.ibosng.dbibosservice.services.impl.SeminarTeilnehmerIbosServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional("mariaDbTransactionManager")
public class SeminarTeilnehmerIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private SeminarTeilnehmerRepository seminarTeilnehmerRepository;

    @Resource
    private SeminarTeilnehmerIbosServiceImpl seminarTeilnehmrService;

    @BeforeEach
    void setUp() {
        SeminarTeilnehmerIbos seminarTeilnehmerIbos = createSeminarTeilnehmer(8, 8);
        seminarTeilnehmrService.save(seminarTeilnehmerIbos);
    }


    private SeminarTeilnehmerIbos createSeminarTeilnehmer(Integer adresseAdnr, Integer seminarSmnr) {
        SeminarTeilnehmerIbos seminarTeilnehmerIbos = new SeminarTeilnehmerIbos();
        seminarTeilnehmerIbos.setSmTnId(new SmTnId(adresseAdnr, seminarSmnr));
        // Set properties as needed
        return seminarTeilnehmerIbos;
    }

    @Test
    void findAll() {
        List<SeminarTeilnehmerIbos> result = seminarTeilnehmrService.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        SeminarTeilnehmerIbos newSeminarTeilnehmerIbos = createSeminarTeilnehmer(1, 1);
        SeminarTeilnehmerIbos saved = seminarTeilnehmerRepository.save(newSeminarTeilnehmerIbos);

        Optional<SeminarTeilnehmerIbos> result = seminarTeilnehmrService.findById(saved.getSmTnId());

        assertTrue(result.isPresent());
        assertEquals(saved.getSmTnId(), result.get().getSmTnId());
    }

    @Test
    void save() {
        SeminarTeilnehmerIbos seminarTeilnehmerIbos = createSeminarTeilnehmer(2, 2);

        SeminarTeilnehmerIbos saved = seminarTeilnehmrService.save(seminarTeilnehmerIbos);

        assertNotNull(saved);
        assertNotNull(saved.getSmTnId());
    }

    @Test
    void saveAll() {
        SeminarTeilnehmerIbos seminarTeilnehmerIbos1 = createSeminarTeilnehmer(3, 3);
        SeminarTeilnehmerIbos seminarTeilnehmerIbos2 = createSeminarTeilnehmer(4, 4);

        List<SeminarTeilnehmerIbos> savedList = seminarTeilnehmrService.saveAll(Arrays.asList(seminarTeilnehmerIbos1, seminarTeilnehmerIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        SeminarTeilnehmerIbos seminarTeilnehmerIbos = createSeminarTeilnehmer(5, 5);
        SeminarTeilnehmerIbos saved = seminarTeilnehmerRepository.save(seminarTeilnehmerIbos);

        SmTnId id = saved.getSmTnId();
        seminarTeilnehmrService.deleteById(id);

        Optional<SeminarTeilnehmerIbos> result = seminarTeilnehmerRepository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void deleteByIdSmTnId() {
        SeminarTeilnehmerIbos seminarTeilnehmerIbos = createSeminarTeilnehmer(6, 6);
        SeminarTeilnehmerIbos saved = seminarTeilnehmerRepository.save(seminarTeilnehmerIbos);

        SmTnId id = saved.getSmTnId();
        seminarTeilnehmrService.deleteById(id);

        Optional<SeminarTeilnehmerIbos> result = seminarTeilnehmerRepository.findById(id);

        assertFalse(result.isPresent());
    }
}
