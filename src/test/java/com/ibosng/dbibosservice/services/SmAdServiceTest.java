package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.smad.SmAd;
import com.ibosng.dbibosservice.entities.smad.SmAdId;
import com.ibosng.dbibosservice.repositories.SmAdRepository;
import com.ibosng.dbibosservice.services.impl.SmAdServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Transactional("mariaDbTransactionManager")
public class SmAdServiceTest extends BaseIntegrationTest {

    @Resource
    private SmAdRepository smAdRepository;

    @Resource
    private SmAdServiceImpl smAdService;

    @BeforeEach
    void setUp() {
        SmAd smAd = createSmAd(555, 777, 123);
        smAdService.save(smAd);
    }

    @Test
    void findAll() {
        List<SmAd> result = smAdService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        SmAd newSmAd = createSmAd(111, 1011, 123);
        SmAd saved = smAdRepository.save(newSmAd);

        Optional<SmAd> result = smAdService.findById(new SmAdId(saved.getSmAdId().getSmadnr(), saved.getSmAdId().getSeminarSmnr()));

        assertTrue(result.isPresent());
        assertEquals(saved.getSmAdId().getSmadnr(), result.get().getSmAdId().getSmadnr());
    }

    @Test
    void save() {
        SmAd smAd = createSmAd(111, 1011, 789);

        SmAd saved = smAdService.save(smAd);

        assertNotNull(saved);
        assertNotNull(saved.getSmAdId().getSmadnr());
    }

    @Test
    void saveAll() {
        SmAd smAd1 = createSmAd(53476, 12124, 1213);
        SmAd smAd2 = createSmAd(5427, 96796, 1617);

        List<SmAd> savedList = smAdService.saveAll(Arrays.asList(smAd1, smAd2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        SmAd smAd = createSmAd(452, 357, 2223);
        SmAd saved = smAdRepository.save(smAd);

        SmAdId id = new SmAdId(saved.getSmAdId().getSmadnr(), saved.getSmAdId().getSeminarSmnr());
        smAdService.deleteById(id);

        Optional<SmAd> result = smAdRepository.findById(id);

        assertFalse(result.isPresent());
    }

    private SmAd createSmAd(Integer smadnr, Integer seminarSmnr, Integer adresseAdadnr) {
        SmAd smAd = new SmAd();
        smAd.setSmAdId(new SmAdId(smadnr, seminarSmnr));
        smAd.setAdresseAdadnr(adresseAdadnr);
        return smAd;
    }

}
