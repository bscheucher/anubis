package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.BenutzerIbos;
import com.ibosng.dbibosservice.repositories.BenutzerIbosRepository;
import com.ibosng.dbibosservice.services.impl.BenutzerIbosServiceImpl;
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
public class BenutzerIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private BenutzerIbosRepository benutzerIbosRepository;

    @Resource
    private BenutzerIbosServiceImpl benutzerService;

    @BeforeEach
    void setUp() {
        BenutzerIbos benutzerIbos = createBenutzerIbos(456654, 123321, "testBefore");
        benutzerService.save(benutzerIbos);
    }


    @Test
    void findAll() {
        List<BenutzerIbos> result = benutzerService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        BenutzerIbos newBenutzerIbos = createBenutzerIbos(123, 456, "test");
        BenutzerIbos saved = benutzerIbosRepository.save(newBenutzerIbos);

        Optional<BenutzerIbos> result = benutzerService.findById(saved.getBnid());

        assertTrue(result.isPresent());
        assertEquals(saved.getBnid(), result.get().getBnid());
    }

    @Test
    void save() {
        BenutzerIbos benutzerIbos = createBenutzerIbos(789, 1011, "test");

        BenutzerIbos saved = benutzerService.save(benutzerIbos);

        assertNotNull(saved);
        assertNotNull(saved.getBnid());
    }

    @Test
    void saveAll() {
        BenutzerIbos benutzerIbos1 = createBenutzerIbos(1213, 1415, "test");
        BenutzerIbos benutzerIbos2 = createBenutzerIbos(1617, 1819, "test");

        List<BenutzerIbos> savedList = benutzerService.saveAll(Arrays.asList(benutzerIbos1, benutzerIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        BenutzerIbos benutzerIbos = createBenutzerIbos(2223, 2021, "test");
        BenutzerIbos saved = benutzerIbosRepository.save(benutzerIbos);

        Integer id = saved.getBnid();
        benutzerService.deleteById(id);

        Optional<BenutzerIbos> result = benutzerIbosRepository.findById(id);

        assertFalse(result.isPresent());
    }

    private BenutzerIbos createBenutzerIbos(Integer bnlistlg, Integer bnadnr, String test) {
        BenutzerIbos benutzerIbos = new BenutzerIbos();
        benutzerIbos.setBnlistlg(bnlistlg);
        benutzerIbos.setBnadnr(bnadnr);
        benutzerIbos.setBnerda(LocalDateTime.now());
        benutzerIbos.setBneruser(test);
        return benutzerIbos;
    }

}
