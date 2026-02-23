package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.pzleistung.Pzleistung;
import com.ibosng.dbibosservice.entities.pzleistung.PzleistungId;
import com.ibosng.dbibosservice.repositories.PzleistungRepository;
import com.ibosng.dbibosservice.services.impl.PzleistungServiceImpl;
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
class PzleistungServiceTest extends BaseIntegrationTest {

    @Resource
    private PzleistungRepository pzleistungRepository;

    @Resource
    private PzleistungServiceImpl pzleistungService;

    @BeforeEach
    void setUp() {
        Pzleistung pzleistung = createpzLeistung(555, 777, 2023, 5, false);
        pzleistungService.save(pzleistung);
    }

    @Test
    void findAll() {
        List<Pzleistung> result = pzleistungService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        Pzleistung newPzLeistung = createpzLeistung(111, 1011, 2021, 2, false);
        Pzleistung saved = pzleistungRepository.save(newPzLeistung);

        Optional<Pzleistung> result = pzleistungService.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(saved.getSmAdSmadnr(), result.get().getSmAdSmadnr());
    }

    @Test
    void save() {
        Pzleistung pzleistung = createpzLeistung(111, 1011, 2024, 8, false);

        Pzleistung saved = pzleistungService.save(pzleistung);

        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void saveAll() {
        Pzleistung pzleistung1 = createpzLeistung(53476, 12124, 2024, 3, false);
        Pzleistung pzleistung2 = createpzLeistung(5427, 96796, 2024, 4, false);

        List<Pzleistung> savedList = pzleistungService.saveAll(Arrays.asList(pzleistung1, pzleistung2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void fetchChangedPZleistungEntries() {
        //In this test only pzleistung 3 and 5 will be collected, because they are the most recently changed/created items in the timespan (31.03 -> 10.04.)
        Pzleistung pzleistung1 = createpzLeistung(101, 3, 2024, 1, true); // Before timeframe created and updated -> not picked up
        Pzleistung pzleistung2 = createpzLeistung(101, 4, 2024, 2, false); // Not picked up
        Pzleistung pzleistung3 = createpzLeistung(101, 1, 2024, 3, true); // Is in march created but changed in april therefore picked up
        Pzleistung pzleistung4 = createpzLeistung(101, 4, 2024, 3, false); // Is in march created by not changed -> not picked up
        Pzleistung pzleistung5 = createpzLeistung(101, 5, 2024, 4, false); // Created in April therefore picked up
        Pzleistung pzleistung6 = createpzLeistung(102, 3, 2024, 4, false); //Different Adnr -> won't be picked up

        LocalDateTime fixedDateTime = LocalDateTime.of(2024, 3, 31, 1, 0);
        List<Pzleistung> savedList = pzleistungService.saveAll(Arrays.asList(pzleistung1, pzleistung2, pzleistung3, pzleistung4, pzleistung5, pzleistung6));
        List<Pzleistung> changedList = pzleistungService.findByADadnrInChangePeriod(101, fixedDateTime, fixedDateTime.plusDays(10));

        assertNotNull(changedList);

        assertEquals(6, savedList.size());
        assertEquals(2, changedList.size());

        assertEquals(101, changedList.get(0).getId().getADadnr());
        assertEquals(1, changedList.get(0).getId().getLZnr());

        assertEquals(101, changedList.get(1).getId().getADadnr());
        assertEquals(5, changedList.get(1).getId().getLZnr());
    }


    private Pzleistung createpzLeistung(Integer pzadnr, Integer lznr, Integer year, Integer month, Boolean changed) {
        Pzleistung pzleistung = new Pzleistung();
        LocalDateTime fixedDateTime = LocalDateTime.of(year, month, 1, 1, 0);
        pzleistung.setId(new PzleistungId(pzadnr, lznr, year, month));
        pzleistung.setLzaeda(fixedDateTime);
        pzleistung.setPzrechnungRenr(1);
        pzleistung.setLztyp("l");
        fixedDateTime = changed ? LocalDateTime.of(year, month + 1, 1, 1, 0) : fixedDateTime;
        pzleistung.setLzerda(fixedDateTime);
        return pzleistung;
    }

}
