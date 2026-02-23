package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.services.ProjektService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.services.impl.ProjektResponseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.ibosng.gatewayservice.enums.PayloadTypes.PROJEKT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ProjektResponseServiceImplTest extends BaseIntegrationTest {

    @InjectMocks
    ProjektResponseServiceImpl projektResponseService;

    @Mock
    ProjektService projektService;

    @Test
    void getAllProjektTest() {

        when(projektService.findByStatus(null, null, null)).thenReturn(List.of("Project1", "Project2", "Project3"));
        PayloadResponse payloadResponse = projektResponseService.getProjektByStatus(null, null, null);

        //then
        assertTrue(payloadResponse.isSuccess());
        PayloadTypeList payloadTypeList = (PayloadTypeList) payloadResponse.getData().get(0);
        assertEquals(PROJEKT.getValue(), payloadTypeList.getType());
        assertEquals(3, payloadTypeList.getAttributes().size());
    }
}
