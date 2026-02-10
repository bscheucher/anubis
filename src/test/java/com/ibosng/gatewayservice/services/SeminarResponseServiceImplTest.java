package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.services.SeminarService;
import com.ibosng.dbservice.services.impl.TeilnehmerServiceImpl;
import com.ibosng.dbservice.services.impl.TeilnehmerStagingServiceImpl;
import com.ibosng.gatewayservice.dtos.response.Pagination;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.impl.SeminarResponseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SeminarResponseServiceImplTest extends BaseIntegrationTest {

    @Mock
    private TeilnehmerServiceImpl teilnehmerService;

    @Mock
    private TeilnehmerStagingServiceImpl teilnehmerStagingService;

    @Mock
    private Gateway2Validation gateway2Validation;

    @Mock
    private PLZOrtService plzOrtService;

    @Mock
    private SeminarService seminarService;

    @InjectMocks
    private SeminarResponseServiceImpl seminarResponseService;

    @Test
    public void testValidateTeilnehmer() {
        when(plzOrtService.addPLZAndOrtAssociation(any(TeilnehmerDto.class), anyString())).thenReturn(true);
        when(gateway2Validation.validateSingleTeilnehmer(any(TeilnehmerDto.class), anyString())).thenReturn(new TeilnehmerDto());

        PayloadResponse response = seminarResponseService.validateTeilnehmer(new TeilnehmerDto(), "Test", Optional.of(true));

        assertNotNull(response);
    }

    @Test
    public void testValidateTeilnehmer_isPlzSavedFalse() {
        when(plzOrtService.addPLZAndOrtAssociation(any(TeilnehmerDto.class), anyString())).thenReturn(false);
        when(gateway2Validation.validateSingleTeilnehmer(any(TeilnehmerDto.class), anyString())).thenReturn(new TeilnehmerDto());

        PayloadResponse response = seminarResponseService.validateTeilnehmer(new TeilnehmerDto(), "Test", Optional.of(true));

        assertNotNull(response);
    }

    @Test
    void testGetAllSeminars() {
        Boolean isUeba = true;
        int page = 0;
        int size = 10;

        BasicSeminarDto seminar1 = new BasicSeminarDto();
        BasicSeminarDto seminar2 = new BasicSeminarDto();

        List<BasicSeminarDto> seminarList = List.of(seminar1, seminar2);
        Page<BasicSeminarDto> mockPage = new PageImpl<>(seminarList, PageRequest.of(page, size), seminarList.size());

        when(seminarService.getAllSeminarsPageable(isUeba, page, size)).thenReturn(mockPage);

        PayloadResponse response = seminarResponseService.getAllSeminars(isUeba, page, size);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());

        PayloadTypeList<BasicSeminarDto> payloadTypeList = (PayloadTypeList<BasicSeminarDto>) response.getData().get(0);

        assertNotNull(payloadTypeList);
        assertEquals(PayloadTypes.SEMINARS.getValue(), payloadTypeList.getType());
        assertEquals(2, payloadTypeList.getAttributes().size());

        Pagination pagination = response.getPagination();
        assertNotNull(pagination);
        assertEquals(size, pagination.getPageSize());
    }

    private Teilnehmer createTeilnehmer() {
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setStatus(TeilnehmerStatus.INVALID);

        return teilnehmer;
    }

    private TeilnehmerDto createTeilnehmerDto() {
        TeilnehmerDto teilnehmerDto = new TeilnehmerDto();
        teilnehmerDto.setSeminarNumber(1);
        teilnehmerDto.setMassnahmennummer("test");

        return teilnehmerDto;
    }

}
