package com.ibosng.gatewayservice.services.impl;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng._service.AsyncService;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbmapperservice.services.ZeitbuchungenMapperService;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.gatewayservice.dtos.GenehmigungDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.EnvironmentService;
import com.ibosng.gatewayservice.services.Gateway2Validation;
import com.ibosng.lhrservice.services.LHRUrlaubService;
import com.ibosng.lhrservice.services.LHRZeitdatenService;
import com.ibosng.lhrservice.services.LHRZeiterfassungService;
import com.ibosng.lhrservice.services.SchedulerService;
import com.ibosng.microsoftgraphservice.services.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZeiterfassungGatewayServiceImplUnitTest {

    @Mock
    private MailService mailService;
    @Mock
    private BenutzerDetailsService benutzerDetailsService;
    @Mock
    private ZeitbuchungenMapperService zeitbuchungenMapper;
    @Mock
    private LeistungserfassungService leistungserfassungService;
    @Mock
    private ZeitbuchungService zeitbuchungService;
    @Mock
    private Gateway2Validation gateway2Validation;
    @Mock
    private PersonalnummerService personalnummerService;
    @Mock
    private AbwesenheitService abwesenheitService;
    @Mock
    private ZeitausgleichService zeitausgleichService;
    @Mock
    private BenutzerService benutzerService;
    @Mock
    private AdresseIbosService adresseIbosService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private AsyncService asyncService;
    @Mock
    private GlobalUserHolder globalUserHolder;
    @Mock
    private LHRUrlaubService lhrUrlaubService;
    @Mock
    private LHRZeiterfassungService lhrZeiterfassungService;
    @Mock
    private SchedulerService schedulerService;
    @Mock
    private LHRZeitdatenService lhrZeitdatenService;

    @InjectMocks
    private ZeiterfassungGatewayServiceImpl zeiterfassungGatewayService;

    private ZeiterfassungGatewayServiceImpl serviceSpy;

    private final String TOKEN = "test-token";
    private final Integer ABWESENHEIT_ID = 123;

    @BeforeEach
    void setUp() {
        serviceSpy = spy(zeiterfassungGatewayService);
    }

    private Benutzer createBenutzer(Integer id, String email) {
        Benutzer user = new Benutzer();
        user.setId(id);
        user.setEmail(email);
        return user;
    }

    private Personalnummer createPersonalnummer(String pn, int bmdClient) {
        Personalnummer personalnummer = new Personalnummer();
        personalnummer.setPersonalnummer(pn);
        IbisFirma firma = new IbisFirma();
        firma.setBmdClient(bmdClient);
        personalnummer.setFirma(firma);
        return personalnummer;
    }

    private Abwesenheit createAbwesenheit(AbwesenheitStatus status, Personalnummer pn, Benutzer fuehrungskraft) {
        Abwesenheit abw = new Abwesenheit();
        abw.setId(ABWESENHEIT_ID);
        abw.setStatus(status);
        abw.setPersonalnummer(pn);
        if (fuehrungskraft != null) {
            abw.setFuehrungskraefte(new HashSet<>(Collections.singletonList(fuehrungskraft)));
        }
        return abw;
    }

    private void stubCommonMocks(Benutzer fuehrungskraft, Benutzer mitarbeiter) {
        when(benutzerDetailsService.getUserFromToken(TOKEN)).thenReturn(fuehrungskraft);
        if (mitarbeiter != null) {
            when(benutzerService.findByPersonalnummerAndFirmaBmdClient(anyString(), anyInt())).thenReturn(mitarbeiter);
        }
    }

    private void assertSuccessPayload(PayloadResponse response, Object expectedDto) {
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        PayloadTypeList<?> payload = (PayloadTypeList<?>) response.getData().get(0);
        assertFalse(payload.getAttributes().isEmpty());
    }

    @Test
    void genehmigungAbwesenheit_shouldAcceptUrlaubRequest_whenValidAndAccepted() {
        // GIVEN
        GenehmigungDto genehmigung = new GenehmigungDto();
        genehmigung.setIsAccepted(true);
        genehmigung.setComment("Approved");

        Benutzer fuehrungskraft = createBenutzer(1, "manager@test.com");
        Personalnummer pn = createPersonalnummer("PN123", 100);
        Abwesenheit abwesenheit = createAbwesenheit(AbwesenheitStatus.VALID, pn, fuehrungskraft);

        AbwesenheitDto abwesenheitDto = AbwesenheitDto.builder()
                .id(ABWESENHEIT_ID)
                .status(AbwesenheitStatus.VALID)
                .type(AbwesenheitType.URLAU)
                .fullName("John Doe")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();

        Benutzer mitarbeiter = createBenutzer(null, "mitarbeiter@test.com");

        when(abwesenheitService.findByIdAndForceRefresh(ABWESENHEIT_ID)).thenReturn(Optional.of(abwesenheit));
        when(abwesenheitService.mapToAbwesenheitDto(any(Abwesenheit.class))).thenReturn(abwesenheitDto);
        stubCommonMocks(fuehrungskraft, mitarbeiter);
        when(abwesenheitService.save(any(Abwesenheit.class))).thenReturn(abwesenheit);

        // WHEN
        PayloadResponse response = serviceSpy.genehmigungAbwesenheit(TOKEN, ABWESENHEIT_ID, genehmigung);

        // THEN
        assertSuccessPayload(response, abwesenheitDto);
        PayloadTypeList<AbwesenheitDto> payloadTypeList = (PayloadTypeList<AbwesenheitDto>) response.getData().get(0);
        assertEquals(PayloadTypes.ABWESENHEIT.getValue(), payloadTypeList.getType());
        assertEquals(abwesenheitDto, payloadTypeList.getAttributes().get(0));

        verify(abwesenheitService, times(2)).findByIdAndForceRefresh(ABWESENHEIT_ID);
        verify(abwesenheitService).save(argThat(abw -> abw.getStatus() == AbwesenheitStatus.ACCEPTED));
        verify(mailService).sendEmail(eq("gateway-service.ma-abwesenheit-genehmigt"), anyString(), isNull(), any(String[].class), any(Object[].class), any(Object[].class));
    }

    @Test
    void genehmigungAbwesenheit_shouldRejectUrlaubRequest_whenValidAndRejected() {
        // GIVEN
        GenehmigungDto genehmigung = new GenehmigungDto();
        genehmigung.setIsAccepted(false);
        genehmigung.setComment("Rejected");

        Benutzer fuehrungskraft = createBenutzer(1, "manager@test.com");
        Personalnummer pn = createPersonalnummer("PN123", 100);
        Abwesenheit abwesenheit = createAbwesenheit(AbwesenheitStatus.VALID, pn, fuehrungskraft);

        AbwesenheitDto abwesenheitDto = AbwesenheitDto.builder()
                .id(ABWESENHEIT_ID)
                .status(AbwesenheitStatus.VALID)
                .type(AbwesenheitType.URLAU)
                .fullName("John Doe")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();

        AbwesenheitDto rejectedDto = abwesenheitDto.toBuilder()
                .status(AbwesenheitStatus.REJECTED)
                .build();

        Benutzer mitarbeiter = createBenutzer(null, "mitarbeiter@test.com");

        when(abwesenheitService.findByIdAndForceRefresh(ABWESENHEIT_ID)).thenReturn(Optional.of(abwesenheit));
        when(abwesenheitService.mapToAbwesenheitDto(any(Abwesenheit.class))).thenAnswer(invocation -> {
            Abwesenheit arg = invocation.getArgument(0);
            if (arg.getStatus() == AbwesenheitStatus.REJECTED) {
                return rejectedDto;
            }
            return abwesenheitDto;
        });
        stubCommonMocks(fuehrungskraft, mitarbeiter);

        // rejectAbwesenheit specific mocks
        when(abwesenheitService.findById(ABWESENHEIT_ID)).thenReturn(Optional.of(abwesenheit));
        when(lhrUrlaubService.deleteUrlaub(any(AbwesenheitDto.class), isNull(), isNull())).thenReturn(ResponseEntity.ok().build());
        when(abwesenheitService.save(any(Abwesenheit.class))).thenReturn(abwesenheit);

        // WHEN
        PayloadResponse response = serviceSpy.genehmigungAbwesenheit(TOKEN, ABWESENHEIT_ID, genehmigung);

        // THEN
        assertSuccessPayload(response, rejectedDto);

        PayloadTypeList<AbwesenheitDto> payloadTypeList = (PayloadTypeList<AbwesenheitDto>) response.getData().get(0);
        assertEquals(AbwesenheitStatus.REJECTED, payloadTypeList.getAttributes().get(0).getStatus());

        verify(lhrUrlaubService).deleteUrlaub(any(AbwesenheitDto.class), isNull(), isNull());
        verify(abwesenheitService).save(argThat(abw -> abw.getStatus() == AbwesenheitStatus.REJECTED));
        verify(mailService).sendEmail(eq("gateway-service.ma-abwesenheit-abgelehnt"), anyString(), isNull(), any(String[].class), any(Object[].class), any(Object[].class));
    }

    @Test
    void genehmigungAbwesenheit_shouldAcceptZeitausgleich_Request_whenValidAndAccepted() {
        // GIVEN
        GenehmigungDto genehmigung = new GenehmigungDto();
        genehmigung.setIsAccepted(true);
        genehmigung.setComment("Approved ZA");

        Benutzer fuehrungskraft = createBenutzer(1, "manager@test.com");
        Personalnummer pn = createPersonalnummer("PN20", 200);
        pn.setId(20);

        Zeitausgleich zeitausgleich = new Zeitausgleich();
        zeitausgleich.setId(10);
        zeitausgleich.setPersonalnummer(pn);
        zeitausgleich.setDatum(LocalDate.now());
        zeitausgleich.setFuehrungskraefte(new HashSet<>(Collections.singletonList(fuehrungskraft)));

        AbwesenheitDto abwesenheitDto = AbwesenheitDto.builder()
                .id(ABWESENHEIT_ID)
                .status(AbwesenheitStatus.VALID)
                .type(AbwesenheitType.ZEITAUSGLEICH)
                .personalnummerId(20)
                .fullName("John Doe")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        AbwesenheitDto acceptedDto = abwesenheitDto.toBuilder()
                .status(AbwesenheitStatus.ACCEPTED)
                .build();

        Benutzer mitarbeiter = createBenutzer(null, "mitarbeiter@test.com");

        // Mocking for genehmigungAbwesenheit entry and Zeitausgleich resolution
        when(abwesenheitService.findByIdAndForceRefresh(ABWESENHEIT_ID)).thenReturn(Optional.empty());
        when(zeitausgleichService.findAllZeitausgleichInPeriod(ABWESENHEIT_ID)).thenReturn(Collections.singletonList(zeitausgleich));
        when(zeitausgleichService.mapListZeitausgleichToListAbwesenheitDto(anyList())).thenAnswer(invocation -> {
            List<Zeitausgleich> list = invocation.getArgument(0);
            if (!list.isEmpty() && list.get(0).getStatus() == AbwesenheitStatus.ACCEPTED) {
                return Collections.singletonList(acceptedDto);
            }
            return Collections.singletonList(abwesenheitDto);
        });
        stubCommonMocks(fuehrungskraft, null);

        // Mocking for acceptZeitausgleich path
        when(zeitausgleichService.save(any(Zeitausgleich.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(benutzerService.findByPersonalnummerId(20)).thenReturn(mitarbeiter);

        // WHEN
        PayloadResponse response = serviceSpy.genehmigungAbwesenheit(TOKEN, ABWESENHEIT_ID, genehmigung);

        // THEN
        assertSuccessPayload(response, Collections.singletonList(acceptedDto));

        PayloadTypeList<List<AbwesenheitDto>> payloadTypeList = (PayloadTypeList<List<AbwesenheitDto>>) response.getData().get(0);
        assertEquals(AbwesenheitStatus.ACCEPTED, payloadTypeList.getAttributes().get(0).get(0).getStatus());

        verify(zeitausgleichService).save(argThat(za -> za.getStatus() == AbwesenheitStatus.ACCEPTED));
        verify(mailService).sendEmail(eq("gateway-service.ma-abwesenheit-genehmigt"), anyString(), isNull(), any(String[].class), any(Object[].class), any(Object[].class));
    }

    @Test
    void genehmigungAbwesenheit_shouldRejectZeitausgleich_Request_whenValidAndRejected() {
        // GIVEN
        GenehmigungDto genehmigung = new GenehmigungDto();
        genehmigung.setIsAccepted(false);
        genehmigung.setComment("Rejected");

        Benutzer fuehrungskraft = createBenutzer(1, "manager@test.com");
        Personalnummer pn = createPersonalnummer("PN20", 200);
        pn.setId(20);

        Zeitausgleich zeitausgleich = new Zeitausgleich();
        zeitausgleich.setId(10);
        zeitausgleich.setPersonalnummer(pn);
        zeitausgleich.setDatum(LocalDate.now());
        zeitausgleich.setFuehrungskraefte(new HashSet<>(Collections.singletonList(fuehrungskraft)));

        AbwesenheitDto abwesenheitDto = AbwesenheitDto.builder()
                .id(ABWESENHEIT_ID)
                .status(AbwesenheitStatus.VALID)
                .type(AbwesenheitType.ZEITAUSGLEICH)
                .personalnummerId(20)
                .fullName("John Doe")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        AbwesenheitDto rejectedDto = abwesenheitDto.toBuilder()
                .status(AbwesenheitStatus.REJECTED)
                .build();

        Benutzer mitarbeiter = createBenutzer(null, "mitarbeiter@test.com");

        // Mocking for genehmigungAbwesenheit
        when(abwesenheitService.findByIdAndForceRefresh(ABWESENHEIT_ID)).thenReturn(Optional.empty());
        when(zeitausgleichService.findAllZeitausgleichInPeriod(ABWESENHEIT_ID)).thenReturn(Collections.singletonList(zeitausgleich));
        when(zeitausgleichService.mapListZeitausgleichToListAbwesenheitDto(anyList())).thenAnswer(invocation -> {
            List<Zeitausgleich> list = invocation.getArgument(0);
            if (!list.isEmpty() && list.get(0).getStatus() == AbwesenheitStatus.REJECTED) {
                return Collections.singletonList(rejectedDto);
            }
            return Collections.singletonList(abwesenheitDto);
        });
        stubCommonMocks(fuehrungskraft, mitarbeiter);

        // Mocking for rejectZeitausgleich path
        when(lhrUrlaubService.deleteZeitausgelich(eq(20), anyString())).thenReturn(ResponseEntity.ok(null));
        when(zeitausgleichService.findByIdAndForceRefresh(10)).thenReturn(Optional.of(zeitausgleich));
        when(zeitausgleichService.save(any(Zeitausgleich.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        PayloadResponse response = serviceSpy.genehmigungAbwesenheit(TOKEN, ABWESENHEIT_ID, genehmigung);

        // THEN
        assertSuccessPayload(response, Collections.singletonList(rejectedDto));

        PayloadTypeList<List<AbwesenheitDto>> payloadTypeList = (PayloadTypeList<List<AbwesenheitDto>>) response.getData().get(0);
        assertEquals(AbwesenheitStatus.REJECTED, payloadTypeList.getAttributes().get(0).get(0).getStatus());

        verify(zeitausgleichService).save(argThat(za -> za.getStatus() == AbwesenheitStatus.REJECTED));
        verify(mailService).sendEmail(eq("gateway-service.ma-abwesenheit-abgelehnt"), anyString(), isNull(), any(String[].class), any(Object[].class), any(Object[].class));
    }

    @Test
    void genehmigungAbwesenheit_shouldReturnError_whenAbwesenheitNotFound() {
        // GIVEN
        GenehmigungDto genehmigung = new GenehmigungDto();
        when(abwesenheitService.findByIdAndForceRefresh(ABWESENHEIT_ID)).thenReturn(Optional.empty());
        when(zeitausgleichService.findAllZeitausgleichInPeriod(ABWESENHEIT_ID)).thenReturn(Collections.emptyList());

        // WHEN
        PayloadResponse response = serviceSpy.genehmigungAbwesenheit(TOKEN, ABWESENHEIT_ID, genehmigung);

        // THEN
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Abwesenheit not found", response.getMessage());

        verify(abwesenheitService).findByIdAndForceRefresh(ABWESENHEIT_ID);
        verify(zeitausgleichService).findAllZeitausgleichInPeriod(ABWESENHEIT_ID);
    }

    @Test
    void genehmigungAbwesenheit_shouldReturnError_whenStatusIsInvalid() {
        // GIVEN
        GenehmigungDto genehmigung = new GenehmigungDto();

        Abwesenheit abwesenheit = new Abwesenheit();
        abwesenheit.setId(ABWESENHEIT_ID);
        abwesenheit.setStatus(AbwesenheitStatus.INVALID);

        AbwesenheitDto abwesenheitDto = AbwesenheitDto.builder()
                .id(ABWESENHEIT_ID)
                .status(AbwesenheitStatus.INVALID)
                .build();

        when(abwesenheitService.findByIdAndForceRefresh(ABWESENHEIT_ID)).thenReturn(Optional.of(abwesenheit));
        when(abwesenheitService.mapToAbwesenheitDto(abwesenheit)).thenReturn(abwesenheitDto);

        // WHEN
        PayloadResponse response = serviceSpy.genehmigungAbwesenheit(TOKEN, ABWESENHEIT_ID, genehmigung);

        // THEN
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Wrong abwesenheit status to genehmigung", response.getMessage());

        verify(abwesenheitService).findByIdAndForceRefresh(ABWESENHEIT_ID);
        verify(abwesenheitService).mapToAbwesenheitDto(abwesenheit);
    }
}
