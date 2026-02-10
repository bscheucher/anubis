package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Bundesland;
import com.ibosng.dbservice.services.impl.BundeslandServiceImpl;
import com.ibosng.dbservice.services.impl.PlzServiceImpl;
import com.ibosng.gatewayservice.services.impl.PLZOrtServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PLZOrtServiceImplTest extends BaseIntegrationTest {

    @Mock
    private PlzServiceImpl plzService;

    @Mock
    private BundeslandServiceImpl bundeslandService;

    @InjectMocks
    private PLZOrtServiceImpl plzOrtService;

    @Test
    public void testAddPLZAndOrtAssociation() {
        when(bundeslandService.findByPlzId(anyInt())).thenReturn(createBundesland());

        Boolean result = plzOrtService.addPLZAndOrtAssociation(createTeilnehmerDto(), "Test");

        assertEquals(true, result);
    }

    @Test
    public void testAddPLZAndOrtAssociationWithNullPlz() {
        Boolean result = plzOrtService.addPLZAndOrtAssociation(new TeilnehmerDto(), "Test");

        assertEquals(result, false);
    }

    @Test
    public void testAddPLZAndOrtAssociationWithNullBundesland() {
        Boolean result = plzOrtService.addPLZAndOrtAssociation(createTeilnehmerDto(), "Test");

        assertEquals(result, false);
    }

    private TeilnehmerDto createTeilnehmerDto() {
        TeilnehmerDto teilnehmerDto = new TeilnehmerDto();

        teilnehmerDto.setPlz("1");
        teilnehmerDto.setOrt("Test");

        return teilnehmerDto;
    }

    private Bundesland createBundesland() {
        Bundesland bundesland = new Bundesland();
        bundesland.setName("Test");

        return bundesland;
    }
}