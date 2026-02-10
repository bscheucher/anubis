package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.repositories.AdresseIbosRepository;
import com.ibosng.dbibosservice.services.impl.AdresseIbosServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional("mariaDbTransactionManager")
public class AdresseIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private AdresseIbosRepository adresseIbosRepository;

    @Resource
    private AdresseIbosServiceImpl adresseIbosService;

    @BeforeEach
    void setUp() {
        AdresseIbos adresseIbos = createAdresseIbos();
        adresseIbosService.save(adresseIbos);
    }

    private AdresseIbos createAdresseIbos() {
        AdresseIbos adresseIbos = new AdresseIbos();
        return adresseIbos;
    }

    @Test
    void findAll() {
        List<AdresseIbos> result = adresseIbosService.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        AdresseIbos newadresseIbos = createAdresseIbos();
        AdresseIbos saved = adresseIbosService.save(newadresseIbos);

        Optional<AdresseIbos> result = adresseIbosService.findById(saved.getAdadnr());

        assertTrue(result.isPresent());
        assertEquals(saved.getAdadnr(), result.get().getAdadnr());
    }

    @Test
    void save() {
        AdresseIbos adresseIbos = createAdresseIbos();

        AdresseIbos saved = adresseIbosService.save(adresseIbos);

        assertNotNull(saved);
        assertNotNull(saved.getAdadnr());
    }

    @Test
    void saveAll() {
        AdresseIbos adresseIbos1 = createAdresseIbos();
        AdresseIbos adresseIbos2 = createAdresseIbos();

        List<AdresseIbos> savedList = adresseIbosService.saveAll(Arrays.asList(adresseIbos1, adresseIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        AdresseIbos adresseIbos = createAdresseIbos();
        AdresseIbos saved = adresseIbosRepository.save(adresseIbos);

        Integer id = saved.getAdadnr();
        adresseIbosService.deleteById(id);

        Optional<AdresseIbos> result = adresseIbosRepository.findById(id);

        assertFalse(result.isPresent());
    }
}
