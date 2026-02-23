package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.StandortIbos;
import com.ibosng.dbibosservice.repositories.StandortIbosRepository;
import com.ibosng.dbibosservice.services.impl.StandortIbosServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional("mariaDbTransactionManager")
public class StandortIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private StandortIbosRepository standortIbosRepository;

    @Resource
    private StandortIbosServiceImpl standortService;

    @BeforeEach
    void setUp() {
        StandortIbos standortIbos = createStandortIbos(123, 456);
        standortService.save(standortIbos);
    }

    @Test
    void findAll() {
        List<StandortIbos> result = standortService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        StandortIbos newStandortIbos = createStandortIbos(123, 456);
        StandortIbos saved = standortIbosRepository.save(newStandortIbos);

        Optional<StandortIbos> result = standortService.findById(saved.getSoStandortId());

        assertTrue(result.isPresent());
        assertEquals(saved.getSoStandortId(), result.get().getSoStandortId());
    }

    @Test
    void save() {
        StandortIbos standortIbos = createStandortIbos(789, 1011);

        StandortIbos saved = standortService.save(standortIbos);

        assertNotNull(saved);
        assertNotNull(saved.getSoStandortId());
    }

    @Test
    void saveAll() {
        StandortIbos standortIbos1 = createStandortIbos(1213, 1415);
        StandortIbos standortIbos2 = createStandortIbos(1617, 1819);

        List<StandortIbos> savedList = standortService.saveAll(Arrays.asList(standortIbos1, standortIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        StandortIbos standortIbos = createStandortIbos(2223, 2021);
        StandortIbos saved = standortIbosRepository.save(standortIbos);

        Integer id = saved.getSoStandortId();
        standortService.deleteById(id);

        Optional<StandortIbos> result = standortIbosRepository.findById(id);

        assertFalse(result.isPresent());
    }

    private StandortIbos createStandortIbos(Integer soGs, Integer soVerantwort) {
        StandortIbos standortIbos = new StandortIbos();
        standortIbos.setSoGs(soGs);
        standortIbos.setSoVerantwort(soVerantwort);
        standortIbos.setSoErda(LocalDateTime.now());
        standortIbos.setSoEruser("test");
        return standortIbos;
    }

}
