package com.ibosng.gatewayservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.dtos.TeilnahmeBasicDto;
import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbibosservice.services.StandortIbosService;
import com.ibosng.dbibosservice.services.TeilnahmeService;
import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerFilterSummaryDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.BasicSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.lhr.Abmeldung;
import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.workflows.*;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import com.ibosng.dbservice.services.lhr.AbmeldungService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungReasonService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungTransferService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.impl.TeilnehmerserviceImpl;
import com.ibosng.lhrservice.services.LHRDienstnehmerService;
import com.ibosng.lhrservice.services.LHRZeiterfassungService;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFStartService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeilnehmerServiceTest extends BaseIntegrationTest {
    @Mock
    private TeilnehmerService teilnehmerService;

    @Mock
    private Gateway2Validation gateway2Validation;

    @Mock
    private ZeiterfassungTransferService zeiterfassungTransferService;

    @Mock
    private LHRDienstnehmerService dienstnehmerService;

    @Mock
    private WFStartService wfStartService;

    @Mock
    private ManageWFsService manageWFsService;

    @Mock
    private ManageWFItemsService manageWFItemsService;

    @Mock
    private MailService mailService;

    @Mock
    private WWorkflowGroupService wWorkflowGroupService;

    @Mock
    private AdresseIbosService adresseIbosService;

    @Mock
    private TeilnehmerStagingService teilnehmerStagingService;

    @Mock
    private BenutzerIbosService benutzerIbosService;

    @Mock
    private StandortIbosService standortIbosService;

    @Mock
    private WWorkflowStatus wWorkflowStatus;

    @Mock
    private BenutzerDetailsService benutzerDetailsService;

    @Mock
    private AbmeldungService abmeldungService;

    @Mock
    private WWorkflowGroup wWorkflowGroup;

    @Mock
    private SWorkflowGroup sWorkflowGroup;

    @Mock
    private TeilnahmeService teilnahmeService;

    @Mock
    private WWorkflowService wWorkflowService;

    @Mock
    private ZeiterfassungReasonService zeiterfassungReasonService;

    @Mock
    private AzureSSOService azureSSOService;

    @Mock
    private LHRZeiterfassungService lhrZeiterfassungService;

    @InjectMocks
    private TeilnehmerserviceImpl teilnehmerservice;

    @Test
    public void testGetTeilnehmerFilterSummaryDto() {
        Benutzer benutzer = new Benutzer();
        benutzer.setId(12);
        String identifiersString = "testIdentifier";
        String seminarName = "testSeminar";
        String projektName = "testProject";
        boolean isActive = true;
        boolean isAngemeldet = true;
        Boolean isUeba = true;
        String geschlecht = "Weiblich";
        String sortProperty = "test";
        String sortDirection = "ASC";
        int page = 0;
        int size = 10;
        List<TeilnehmerFilterSummaryDto> teilnehmerList = Collections.singletonList(new TeilnehmerFilterSummaryDto());
        Page<TeilnehmerFilterSummaryDto> mockPage = new PageImpl<>(teilnehmerList, PageRequest.of(page, size, Sort.by(sortProperty)), teilnehmerList.size());
        when(teilnehmerService.findTeilnehmerFiltered(anyString(), anyString(), anyString(), anyBoolean(), any(Boolean.class), anyBoolean(), anyString(), any(Boolean.class), anyString(), any(Integer.class), anyString(), any(Sort.Direction.class), anyInt(), anyInt()))
                .thenReturn(mockPage);
        PayloadResponse response = teilnehmerservice.getTeilnehmerFilterSummaryDto(identifiersString, seminarName, projektName, isActive, isUeba, isAngemeldet, geschlecht, Boolean.TRUE, "", benutzer.getId(), sortProperty, sortDirection, page, size);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetTeilnehmerFilterSummaryDtoWithNullSortProperty() {
        String identifiersString = "testIdentifier";
        String seminarName = "testSeminar";
        String projektName = "testProject";
        boolean isActive = true;
        boolean isAngemeldet = true;
        Boolean isUeba = true;
        String geschlecht = "Weiblich";
        String sortProperty = "test";
        String sortDirection = "ASC";
        int page = 0;
        int size = 10;
        List<TeilnehmerFilterSummaryDto> teilnehmerList = Collections.singletonList(new TeilnehmerFilterSummaryDto());
        Page<TeilnehmerFilterSummaryDto> mockPage = new PageImpl<>(teilnehmerList, PageRequest.of(page, size, Sort.by(sortProperty)), teilnehmerList.size());
        when(teilnehmerService.findTeilnehmerFiltered(
                anyString(),
                anyString(),
                anyString(),
                anyBoolean(),
                any(Boolean.class),
                anyBoolean(),
                anyString(),
                any(Boolean.class),
                anyString(),
                any(Integer.class),
                anyString(),
                any(Sort.Direction.class),
                anyInt(),
                anyInt()))
                .thenReturn(mockPage);
        PayloadResponse response = teilnehmerservice.getTeilnehmerFilterSummaryDto(identifiersString, seminarName, projektName, isActive, isUeba, isAngemeldet, geschlecht, Boolean.TRUE, "", 12, null, sortDirection, page, size);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetTeilnehmerById() {
        when(teilnehmerService.findTeilnehmerDtoById(anyInt())).thenReturn(new TeilnehmerSeminarDto());
        PayloadResponse response = teilnehmerservice.getTeilnehmerById(123, false, null);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetTeilnehmerByIdWithNullTeilnehmerDto() {
        when(teilnehmerService.findTeilnehmerDtoById(anyInt())).thenReturn(null);
        PayloadResponse response = teilnehmerservice.getTeilnehmerById(123, false, null);
        assertNull(response);
    }

    @Test
    public void testValidateTeilnehmer() {
        TeilnehmerDto teilnehmerDto = new TeilnehmerDto();
        teilnehmerDto.setId(123);
        TeilnehmerSeminarDto teilnehmerSeminarDto = new TeilnehmerSeminarDto();
        teilnehmerSeminarDto.setTeilnehmerDto(teilnehmerDto);

        Benutzer benutzer = new Benutzer();
        benutzer.setEmail("test@email.com");
        when(benutzerDetailsService.getUserFromToken(anyString())).thenReturn(benutzer);
        when(gateway2Validation.validateTeilnehmerDto(any(TeilnehmerDto.class), anyString())).thenReturn(teilnehmerDto);
        when(teilnehmerService.findTeilnehmerDtoById(anyInt())).thenReturn(new TeilnehmerSeminarDto());
        PayloadResponse response = teilnehmerservice.validateTeilnehmer(teilnehmerSeminarDto, "test");
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetTeilnehmersZeiterfassungWithNullShouldSubmit() {
        TeilnahmeBasicDto teilnahmeBasicDto = new TeilnahmeBasicDto();
        teilnahmeBasicDto.setTeilnehmerNumber(1);
        teilnahmeBasicDto.setSeminars(List.of(1));

        when(gateway2Validation.validateTeilnehmersZeiterfassung(any(ZeiterfassungTransferDto.class), anyString()))
                .thenReturn(Mono.just(true)); // Return a Mono<Boolean> as expected
        when(zeiterfassungReasonService.findAllReasonsWithIbosidNotNull()).thenReturn(List.of(1, 2));
        when(teilnahmeService.getTeilnehmerSeminarSummary(anyList(), anyList(), any(LocalDate.class), any(LocalDate.class))).thenReturn(teilnahmeBasicDto);
        PayloadResponse response = teilnehmerservice.getTeilnehmersZeiterfassung(createZeiterfassungTransferDto(), false, "test");
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetTeilnehmersZeiterfassungWithOKResponseFromLHRSerbive() {
        ResponseEntity<ZeiterfassungTransferDto> responseEntity = new ResponseEntity<>(new ZeiterfassungTransferDto(), HttpStatus.OK);
        ZeiterfassungTransfer zeiterfassungTransfer = new ZeiterfassungTransfer();
        zeiterfassungTransfer.setId(123);
        WWorkflow wWorkflow = new WWorkflow();
        wWorkflow.setWorkflowGroup(new WWorkflowGroup());
        when(gateway2Validation.validateTeilnehmersZeiterfassung(any(ZeiterfassungTransferDto.class), anyString()))
                .thenReturn(Mono.just(true)); // Return a Mono<Boolean> as expected
        when(zeiterfassungTransferService.findById(anyInt())).thenReturn(Optional.of(zeiterfassungTransfer));
        when(zeiterfassungTransferService.save(any(ZeiterfassungTransfer.class))).thenReturn(zeiterfassungTransfer);
        when(wfStartService.createWorkflowGroupAndInstances(anyString(), anyString(), anyString())).thenReturn(new WWorkflowGroup());
        when(manageWFsService.setWFStatus(any(WWorkflowGroup.class), any(SWorkflows.class), any(WWorkflowStatus.class), anyString())).thenReturn(wWorkflow);
        when(lhrZeiterfassungService.sendZeiterfassungTransfer(anyString(), anyString())).thenReturn(responseEntity);
        when(zeiterfassungTransferService.mapZeiterfassungTransferToDto(any(ZeiterfassungTransfer.class))).thenReturn(new ZeiterfassungTransferDto());
        PayloadResponse response = teilnehmerservice.getTeilnehmersZeiterfassung(generateZeiterfassungTransferDto(), true, "test");
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetTeilnehmersZeiterfassungWithFailureResponseFromLHRSerbive() {
        ResponseEntity<ZeiterfassungTransferDto> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        ZeiterfassungTransfer zeiterfassungTransfer = new ZeiterfassungTransfer();
        zeiterfassungTransfer.setId(123);
        when(gateway2Validation.validateTeilnehmersZeiterfassung(any(ZeiterfassungTransferDto.class), anyString()))
                .thenReturn(Mono.just(true)); // Return a Mono<Boolean> as expected
        when(zeiterfassungTransferService.findById(anyInt())).thenReturn(Optional.of(zeiterfassungTransfer));
        when(zeiterfassungTransferService.save(any(ZeiterfassungTransfer.class))).thenReturn(zeiterfassungTransfer);
        when(wfStartService.createWorkflowGroupAndInstances(anyString(), anyString(), anyString())).thenReturn(new WWorkflowGroup());
        when(manageWFsService.setWFStatus(any(WWorkflowGroup.class), any(SWorkflows.class), any(WWorkflowStatus.class), anyString())).thenReturn(new WWorkflow());
        when(lhrZeiterfassungService.sendZeiterfassungTransfer(anyString(), anyString())).thenReturn(responseEntity);
        when(zeiterfassungTransferService.mapZeiterfassungTransferToDto(any(ZeiterfassungTransfer.class))).thenReturn(new ZeiterfassungTransferDto());
        PayloadResponse response = teilnehmerservice.getTeilnehmersZeiterfassung(generateZeiterfassungTransferDto(), true, "test");
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetZeiterfassungTransfers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ZeiterfassungTransferDto> dtoList = Collections.singletonList(new ZeiterfassungTransferDto());
        Page<ZeiterfassungTransferDto> mockPage = new PageImpl<>(dtoList, pageable, dtoList.size());
        when(zeiterfassungTransferService.findAllDtos(any(Pageable.class))).thenReturn(mockPage);
        PayloadResponse response = teilnehmerservice.getZeiterfassungTransfers("test", "test", 1, 1);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetFilteredTeilnehmer() {
        String vorname = "Max";
        String nachname = "Mustermann";
        String sortProperty = "name";
        String sortDirection = "ASC";
        int page = 0;
        int size = 10;
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setVorname("Max");
        teilnehmer.setNachname("Mustermann");
        List<Teilnehmer> teilnehmerList = Collections.singletonList(teilnehmer);
        Page<Teilnehmer> mockPage = new PageImpl<>(teilnehmerList, pageable, teilnehmerList.size());
        when(teilnehmerService.findUebaTeilnehmerByCriteria(anyString(), anyString(), any(Sort.Direction.class), anyInt(), anyInt()))
                .thenReturn(mockPage);
        when(benutzerIbosService.getTitelFromId(anyInt())).thenReturn("Test");
        when(standortIbosService.getLandIdFromId(anyInt())).thenReturn("Test");
        when(teilnehmerStagingService.findByVornameAndNachname(anyString(), anyString())).thenReturn(List.of(new TeilnehmerStaging()));
        PayloadResponse response = teilnehmerservice.getFilteredTeilnehmer(vorname, nachname, "test", page, size);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetFilteredTeilnehmerWithEmptyTeilnehmerDtoList() {
        String vorname = "Max";
        String nachname = "Mustermann";
        String sortProperty = "vorname";
        String sortDirection = "ASC";
        int page = 0;
        int size = 10;
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setVorname("Max");
        teilnehmer.setNachname("Mustermann");
        List<Teilnehmer> teilnehmerList = new ArrayList<>();
        Page<Teilnehmer> mockPage = new PageImpl<>(teilnehmerList, pageable, 0);
        when(teilnehmerService.findUebaTeilnehmerByCriteria(anyString(), anyString(), any(Sort.Direction.class), anyInt(), anyInt()))
                .thenReturn(mockPage);
        when(benutzerIbosService.getTitelFromId(anyInt())).thenReturn("Test");
        when(standortIbosService.getLandIdFromId(anyInt())).thenReturn("Test");
        when(teilnehmerStagingService.findByVornameAndNachname(anyString(), anyString())).thenReturn(List.of(new TeilnehmerStaging()));
        when(adresseIbosService.getFilteredTeilnehmer(anyString(), anyString(), anyString())).thenReturn(List.of(generateAdresseIbos()));
        PayloadResponse response = teilnehmerservice.getFilteredTeilnehmer(vorname, nachname, "test", page, size);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    public void testPostUebaAbmeldungSuccess() {
        String token = "validToken";
        AbmeldungDto abmeldungDto = new AbmeldungDto();
        abmeldungDto.setTeilnehmerId(123);
        abmeldungDto.setAustrittsDatum("2024-11-20");

        WWorkflowGroup workflowGroup = new WWorkflowGroup();
        workflowGroup.setId(1);
        workflowGroup.setCreatedBy("Max");
        workflowGroup.setWorkflowGroup(sWorkflowGroup);

        Benutzer benutzer = new Benutzer();
        benutzer.setCreatedBy("Max");
        benutzer.setEmail("email@test.com");

        Abmeldung abmeldung = new Abmeldung();
        abmeldung.setId(456);

        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setVorname("Max");
        teilnehmer.setNachname("Mustermann");
        List<WWorkflow> workflowList = new ArrayList<>();
        WWorkflow wWorkflow = new WWorkflow();
        SWorkflow sWorkflow = new SWorkflow();
        sWorkflow.setName(SWorkflows.TN_TRITT_SICHERLICH_AUS.getValue());
        wWorkflow.setWorkflow(sWorkflow);
        workflowList.add(wWorkflow);

        when(benutzerDetailsService.getUserFromToken(token)).thenReturn(benutzer);
        when(abmeldungService.mapToAbmeldung(abmeldungDto, AbmeldungStatus.NEW)).thenReturn(abmeldung);
        when(abmeldungService.save(abmeldung)).thenReturn(abmeldung);
        when(wfStartService.createWorkflowGroupAndInstances(anyString(), anyString(), anyString())).thenReturn(new WWorkflowGroup());
        when(wWorkflowGroupService.save(any(WWorkflowGroup.class))).thenReturn(new WWorkflowGroup());
        when(manageWFsService.setWFStatus(any(), any(), any(), any())).thenReturn(new WWorkflow());
        when(teilnehmerService.findById(123)).thenReturn(Optional.of(teilnehmer));
        when(dienstnehmerService.mapAndSendUebaAbmeldung(abmeldungDto)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(wWorkflowService.findAllByWorkflowGroup(any())).thenReturn(workflowList);
        when(azureSSOService.getGroupMemberEmailsByName(anyString())).thenReturn(List.of("email@test.com"));

        PayloadResponse response = teilnehmerservice.postUebaAbmeldung(token, abmeldungDto);

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        verify(mailService).sendEmail(eq("gateway-service.lhr.tn-ueba-abmelden"), eq("german"), any(), any(), any(), any());
        verify(dienstnehmerService).mapAndSendUebaAbmeldung(abmeldungDto);
    }

    @Test
    public void testGetUebaAbmeldung() {
        int page = 0;
        int size = 10;
        AbmeldungDto abmeldungDto = new AbmeldungDto();
        abmeldungDto.setId(1);
        abmeldungDto.setAustrittsDatum(String.valueOf(LocalDate.now()));

        List<AbmeldungDto> abmeldungDtoList = Collections.singletonList(abmeldungDto);
        Page<AbmeldungDto> mockPage = new PageImpl<>(abmeldungDtoList, PageRequest.of(page, size), abmeldungDtoList.size());

        when(abmeldungService.findAll(PageRequest.of(page, size))).thenReturn(mockPage);

        PayloadResponse response = teilnehmerservice.getUebaAbmeldung(page, size);

        assertTrue(response.isSuccess(), "Response should be successful");
        assertNotNull(response.getData(), "Response data should not be null");
        assertFalse(response.getData().isEmpty(), "Response data should not be empty");

        assertNotNull(response.getPagination(), "Pagination should not be null");
        assertEquals(page, response.getPagination().getPage(), "Pagination page should match");
    }

    @Test
    public void testGetUebaAbmeldungById_Exists() {
        Integer id = 1;
        Abmeldung abmeldung = new Abmeldung();
        abmeldung.setId(id);
        abmeldung.setAustrittsDatum(LocalDate.now());

        AbmeldungDto abmeldungDto = new AbmeldungDto();
        abmeldungDto.setId(id);
        abmeldungDto.setAustrittsDatum(String.valueOf(LocalDate.now()));

        when(abmeldungService.findById(id)).thenReturn(Optional.of(abmeldung));
        when(abmeldungService.mapToAbmeldungDto(abmeldung)).thenReturn(abmeldungDto);

        PayloadResponse response = teilnehmerservice.getUebaAbmeldungById(id);

        assertTrue(response.isSuccess(), "Response should be successful");
        assertNotNull(response.getData(), "Response data should not be null");
        assertEquals(1, response.getData().size(), "Response data size should be 1");
        assertNotNull(response.getData().get(0), "Payload should not be null");
        assertTrue(response.getData().get(0) instanceof PayloadTypeList, "First element should be PayloadTypeList");
        assertEquals(PayloadTypes.UEBAABMELDUNGEN.getValue(),
                ((PayloadTypeList<?>) response.getData().get(0)).getType(),
                "PayloadType should match");

        PayloadTypeList<AbmeldungDto> payloadTypeList = (PayloadTypeList<AbmeldungDto>) response.getData().get(0);
        assertNotNull(payloadTypeList.getAttributes(), "Attributes should not be null");
        assertEquals(1, payloadTypeList.getAttributes().size(), "Attributes size should be 1");
        assertEquals(abmeldungDto, payloadTypeList.getAttributes().get(0), "Attributes should contain the correct AbmeldungDto");
    }

    @Test
    public void testGetUebaAbmeldungById_NotFound() {
        Integer id = 1;

        when(abmeldungService.findById(id)).thenReturn(Optional.empty());

        PayloadResponse response = teilnehmerservice.getUebaAbmeldungById(id);

        assertFalse(response.isSuccess(), "Response should be unsuccessful");
        assertNotNull(response.getData(), "Response data should not be null");
        assertTrue(response.getData().isEmpty(), "Response data should be empty");
    }

    private ZeiterfassungTransferDto generateZeiterfassungTransferDto() {
        ZeiterfassungTransferDto zeiterfassungTransferDto = new ZeiterfassungTransferDto();
        zeiterfassungTransferDto.setId(123);
        zeiterfassungTransferDto.setDatumVon("01/01");
        zeiterfassungTransferDto.setDatumBis("01/01");
        return zeiterfassungTransferDto;
    }

    private AdresseIbos generateAdresseIbos() {
        AdresseIbos adresseIbos = new AdresseIbos();
        adresseIbos.setAdvnf2("Test");
        adresseIbos.setAdznf1("Test");
        adresseIbos.setAdgebdatum(LocalDate.now());
        adresseIbos.setAdmobil1("Test");
        adresseIbos.setAdsvnr("Test");
        adresseIbos.setAdemail1("Test");
        adresseIbos.setAdgeschlecht("Test");
        adresseIbos.setAdort("Test");
        adresseIbos.setAdplz("Test");
        adresseIbos.setAdstrasse("Test");
        adresseIbos.setAdtitel(123);
        adresseIbos.setAdtitelv(123);
        adresseIbos.setAdstaatsb(123);
        return adresseIbos;
    }

    private ZeiterfassungTransferDto createZeiterfassungTransferDto() {
        ZeiterfassungTransferDto zeiterfassungTransferDto = new ZeiterfassungTransferDto();
        zeiterfassungTransferDto.setDatumBis("10.11.2020");
        zeiterfassungTransferDto.setDatumVon("11.11.2020");

        BasicSeminarDto seminar = new BasicSeminarDto();
        seminar.setSeminarNumber(1);
        zeiterfassungTransferDto.setSeminars(List.of(seminar));

        return zeiterfassungTransferDto;
    }

}