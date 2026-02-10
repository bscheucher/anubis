package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.AusschreibungIbos;
import com.ibosng.dbibosservice.repositories.AusschreibungIbosRepository;
import com.ibosng.dbibosservice.services.impl.AusschreibungIbosServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional("mariaDbTransactionManager")
public class AusschreibungIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private AusschreibungIbosRepository ausschreibungIbosRepository;

    @Resource
    private AusschreibungIbosServiceImpl ausschreibungIbosService;

    @BeforeEach
    void setUp() {
        AusschreibungIbos ausschreibungIbos = createAusschreibungIbos(1);
        ausschreibungIbosService.save(ausschreibungIbos);
    }

    private AusschreibungIbos createAusschreibungIbos(Integer asbundesland) {
        AusschreibungIbos ausschreibungIbos = new AusschreibungIbos();
        ausschreibungIbos.setAsbundesland(asbundesland);
        return ausschreibungIbos;
    }

    @Test
    void findAll() {
        List<AusschreibungIbos> result = ausschreibungIbosService.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        AusschreibungIbos newAusschreibungIbos = createAusschreibungIbos(2);
        AusschreibungIbos saved = ausschreibungIbosRepository.save(newAusschreibungIbos);

        Optional<AusschreibungIbos> result = ausschreibungIbosService.findById(saved.getAsnr());

        assertTrue(result.isPresent());
        assertEquals(saved.getAsnr(), result.get().getAsnr());
    }

    @Test
    void save() {
        AusschreibungIbos AusschreibungIbos = createAusschreibungIbos(3);

        AusschreibungIbos saved = ausschreibungIbosService.save(AusschreibungIbos);

        assertNotNull(saved);
        assertNotNull(saved.getAsnr());
    }

    @Test
    void saveAll() {
        AusschreibungIbos AusschreibungIbos1 = createAusschreibungIbos(4);
        AusschreibungIbos AusschreibungIbos2 = createAusschreibungIbos(5);

        List<AusschreibungIbos> savedList = ausschreibungIbosService.saveAll(Arrays.asList(AusschreibungIbos1, AusschreibungIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        AusschreibungIbos ausschreibungIbos = createAusschreibungIbos(6);
        AusschreibungIbos saved = ausschreibungIbosRepository.save(ausschreibungIbos);

        Integer id = saved.getAsnr();
        ausschreibungIbosService.deleteById(id);

        Optional<AusschreibungIbos> result = ausschreibungIbosRepository.findById(id);

        assertFalse(result.isPresent());
    }
}
