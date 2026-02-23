package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.entities.SeminarbuchIbos;
import com.ibosng.dbibosservice.repositories.SeminarbuchIbosRepository;
import com.ibosng.dbibosservice.services.impl.SeminarbuchIbosServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Transactional("mariaDbTransactionManager")
public class SeminarbuchIbosServiceTest extends BaseIntegrationTest {

    @Resource
    private SeminarbuchIbosRepository seminarbuchIbosRepository;

    @Resource
    private SeminarbuchIbosServiceImpl seminarbuchService;

    @BeforeEach
    void setUp() {
        SeminarbuchIbos seminarbuchIbos = createSeminarbuchIbos(123, 456);
        seminarbuchService.save(seminarbuchIbos);
    }

    @Test
    void findAll() {
        List<SeminarbuchIbos> result = seminarbuchService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void findById() {
        SeminarbuchIbos newSeminarbuchIbos = createSeminarbuchIbos(123, 456);
        SeminarbuchIbos saved = seminarbuchIbosRepository.save(newSeminarbuchIbos);

        Optional<SeminarbuchIbos> result = seminarbuchService.findById(saved.getSbnr());

        assertTrue(result.isPresent());
        assertEquals(saved.getSbnr(), result.get().getSbnr());
    }

    @Test
    void save() {
        SeminarbuchIbos seminarbuchIbos = createSeminarbuchIbos(789, 1011);

        SeminarbuchIbos saved = seminarbuchService.save(seminarbuchIbos);

        assertNotNull(saved);
        assertNotNull(saved.getSbnr());
    }

    @Test
    void saveAll() {
        SeminarbuchIbos seminarbuchIbos1 = createSeminarbuchIbos(1213, 1415);
        SeminarbuchIbos seminarbuchIbos2 = createSeminarbuchIbos(1617, 1819);

        List<SeminarbuchIbos> savedList = seminarbuchService.saveAll(Arrays.asList(seminarbuchIbos1, seminarbuchIbos2));

        assertNotNull(savedList);
        assertEquals(2, savedList.size());
    }

    @Test
    void deleteById() {
        SeminarbuchIbos seminarbuchIbos = createSeminarbuchIbos(2021, 2223);
        SeminarbuchIbos saved = seminarbuchIbosRepository.save(seminarbuchIbos);

        Integer id = saved.getSbnr();
        seminarbuchService.deleteById(id);

        Optional<SeminarbuchIbos> result = seminarbuchIbosRepository.findById(id);

        assertFalse(result.isPresent());
    }

    private SeminarbuchIbos createSeminarbuchIbos(Integer adadnr, Integer smnr) {
        SeminarbuchIbos seminarbuchIbos = new SeminarbuchIbos();
        seminarbuchIbos.setAdadnr(adadnr);
        seminarbuchIbos.setSmnr(smnr);
        return seminarbuchIbos;
    }
}
