package com.ibosng.dbibosservice.services;


import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.PzMonat;
import com.ibosng.dbibosservice.entities.PzMonatId;
import com.ibosng.dbibosservice.repositories.PzMonatRepository;
import com.ibosng.dbibosservice.services.impl.PzMonatServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional("mariaDbTransactionManager")
public class PzMonatServiceImplTest extends BaseIntegrationTest {

    @Resource
    private PzMonatRepository pzMonatRepository;

    @Resource
    private PzMonatServiceImpl pzMonatService;

    private PzMonat pzMonat;

    @BeforeEach
    void setUp() {
        pzMonat = createPzMonat(123, 2024, 10);
        pzMonatService.save(pzMonat);
    }

    @Test
    void findAll() {
        List<PzMonat> result = pzMonatService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        PzMonatId id = pzMonat.getId();
        Optional<PzMonat> result = pzMonatService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void save() {
        PzMonat newPzMonat = createPzMonat(456, 2024, 11);

        PzMonat saved = pzMonatService.save(newPzMonat);

        assertNotNull(saved);
        assertEquals(456, saved.getId().getAdAdnr());
        assertEquals(2024, saved.getId().getJahr());
        assertEquals(11, saved.getId().getMonat());
    }

    @Test
    void saveAll() {
        PzMonat pzMonat1 = createPzMonat(789, 2024, 12);
        PzMonat pzMonat2 = createPzMonat(1011, 2025, 1);

        List<PzMonat> savedList = pzMonatService.saveAll(Arrays.asList(pzMonat1, pzMonat2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void findByAdAdnrJahrAndMonat() {
        PzMonat result = pzMonatService.findByAdAdnrJahrAndMonat(123, 2024, 10);

        assertNotNull(result);
        assertEquals(123, result.getId().getAdAdnr());
        assertEquals(2024, result.getId().getJahr());
        assertEquals(10, result.getId().getMonat());
    }

    private PzMonat createPzMonat(Integer adAdnr, Integer jahr, Integer monat) {
        PzMonat pzmonat = new PzMonat();
        PzMonatId id = new PzMonatId(adAdnr, jahr, monat);
        pzmonat.setId(id);
        pzmonat.setPmStatus(1);
        return pzmonat;
    }
}
