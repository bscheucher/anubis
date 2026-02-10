package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.services.impl.BenutzerServiceImpl;
import com.ibosng.dbservice.services.impl.SeminarServiceImpl;
import com.ibosng.gatewayservice.services.impl.DatasetFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


public class DatasetFactoryTest extends BaseIntegrationTest {

    @Mock
    private BenutzerServiceImpl benutzerService;

    @Mock
    private SeminarServiceImpl seminarService;

    @InjectMocks
    private DatasetFactory datasetFactory;

    private String BENUTZER_KEY = "benutzer";
    private String SEMINAR_KEY = "seminar";
    private String INVALID_KEY = "invalid key";

    @Test
    public void testGetDataSetWithBenutzerKey() {
        Benutzer benutzer = new Benutzer();
        List<Benutzer> benutzerList = List.of(benutzer);

        when(benutzerService.findAll()).thenReturn(benutzerList);

        List<Object> result = datasetFactory.getDataSet(BENUTZER_KEY);

        assertEquals(benutzerList.size(), result.size());
    }

    @Test
    public void testGetDataSetWithSeminarKey() {
        Seminar seminar = new Seminar();
        List<Seminar> seminarList = List.of(seminar);

        when(seminarService.findAll()).thenReturn(seminarList);

        List<Object> result = datasetFactory.getDataSet(SEMINAR_KEY);

        assertEquals(seminarList.size(), result.size());
    }

    @Test
    public void testGetDataSetWithInvalidKey() {
        List<Object> result = datasetFactory.getDataSet(INVALID_KEY);

        assertNull(result);
    }
}
