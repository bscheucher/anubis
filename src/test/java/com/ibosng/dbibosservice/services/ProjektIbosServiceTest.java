package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.ProjektIbos;
import com.ibosng.dbibosservice.repositories.ProjektIbosRepository;
import com.ibosng.dbibosservice.services.impl.ProjektIbosServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional("mariaDbTransactionManager")
public class ProjektIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private ProjektIbosRepository projektIbosRepository;

    @Resource
    private ProjektIbosServiceImpl projektIbosService;

    @BeforeEach
    void setUp() {
        ProjektIbos projektIbos = createProjektIbos(1);
        projektIbosService.save(projektIbos);
    }

    private ProjektIbos createProjektIbos(Integer asnr) {
        ProjektIbos projektIbos = new ProjektIbos();
        projektIbos.setAsNr(asnr);
        return projektIbos;
    }

    @Test
    void findAll() {
        List<ProjektIbos> result = projektIbosService.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        ProjektIbos newProjektIbos = createProjektIbos(2);
        ProjektIbos saved = projektIbosRepository.save(newProjektIbos);

        Optional<ProjektIbos> result = projektIbosService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
    }

    @Test
    void save() {
        ProjektIbos ProjektIbos = createProjektIbos(3);

        ProjektIbos saved = projektIbosService.save(ProjektIbos);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void saveAll() {
        ProjektIbos ProjektIbos1 = createProjektIbos(4);
        ProjektIbos ProjektIbos2 = createProjektIbos(5);

        List<ProjektIbos> savedList = projektIbosService.saveAll(Arrays.asList(ProjektIbos1, ProjektIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        ProjektIbos ProjektIbos = createProjektIbos(6);
        ProjektIbos saved = projektIbosRepository.save(ProjektIbos);

        Integer id = saved.getId();
        projektIbosService.deleteById(id);

        Optional<ProjektIbos> result = projektIbosRepository.findById(id);

        assertFalse(result.isPresent());
    }
}
