package com.ibosng.personalverwaltung;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibosng.BaseIntegrationTest;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng._config.GlobalWebConfigTest;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.repositories.BenutzerRepository;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.dbservice.repositories.masterdata.IbisFirmaRepository;
import com.ibosng.dbservice.repositories.mitarbeiter.PersonalnummerRepository;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.EnvironmentService;
import com.ibosng.gatewayservice.services.impl.BenutzerDetailsServiceImpl;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntryRepository;
import com.ibosng.personalverwaltung.utils.BenutzerFactory;
import com.ibosng.personalverwaltung.utils.IbisFirmaFactory;
import com.ibosng.personalverwaltung.utils.PersonalnummerFactory;
import com.ibosng.personalverwaltung.web.AbwesenheitCreateV2Dto;
import com.ibosng.personalverwaltung.web.AbwesenheitV2Dto;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.ibosng.gatewayservice.utils.Constants.FN_ABWESENHEITEN_EDITIEREN;
import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Operation.CREATE_ABWESENHEIT_REQUEST;
import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Status.DONE;
import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Status.NEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Import(GlobalWebConfigTest.class)
public class AbwesenheitenV2IntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private IbisFirmaRepository ibisFirmaRepository;
    @Autowired
    private PersonalnummerRepository personalnummerRepository;
    @Autowired
    private AbwesenheitRespository abwesenheitRespository;
    @Autowired
    private LhrOutboxEntryRepository lhrOutboxEntryRepository;
    @Autowired
    private BenutzerRepository benutzerRepository;

    @MockBean
    private BenutzerDetailsServiceImpl benutzerDetailsServiceMock;
    @MockBean
    private EnvironmentService environmentServiceMock;
    @MockBean
    private LeistungserfassungService leistungserfassungService;
    @MockBean
    private ZeitbuchungService zeitbuchungService;
    @MockBean
    private AbwesenheitService abwesenheitService;
    @MockBean
    private ZeitausgleichService zeitausgleichService;
    @MockBean
    private AdresseIbosService adresseIbosServiceMock;

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private Personalnummer personalnummer;


    @BeforeEach
    void setUp() {

        var benutzerUpn = "test.user@somethingirrelevant.com";
        personalnummer = persistAndGetCompletePersonalnummerWithBenutzer(benutzerUpn);

        var mockFuehrungskraftUpn = "fk.user@example.com";
        benutzerRepository.save(BenutzerFactory.createForUpn(mockFuehrungskraftUpn));
        when(adresseIbosServiceMock.getFuehrungskraftUPNFromLogin("test.user")).thenReturn(mockFuehrungskraftUpn);

        // testing of token validity behavior could be done at later point in time
        when(benutzerDetailsServiceMock.isUserEligible("some token", List.of(FN_ABWESENHEITEN_EDITIEREN))).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        abwesenheitRespository.deleteAll();
        lhrOutboxEntryRepository.deleteAll();
        benutzerRepository.deleteAll();
        personalnummerRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void personalNummerIncomplete_createUrlaubRequest_unauthorized() {

        var irrelevantDto = new AbwesenheitCreateV2Dto();
        var personalnummerWithoutFirma = new Personalnummer();
        personalnummerWithoutFirma.setPersonalnummer("123456789");
        when(environmentServiceMock.checkLoggedInUserAndGetPersonalnummer()).thenReturn(personalnummerWithoutFirma);

        performRequest(irrelevantDto)
                .andExpect(status().isUnauthorized());

    }

    @Test
    @SneakyThrows
    @Description("Tests unchanged msg-plaut logic")
    void requestInvalid_createUrlaubRequest_200withSuccessFalse() {

        var dtoInvalidSinceNoStartAndEndDates = new AbwesenheitCreateV2Dto();
        when(environmentServiceMock.checkLoggedInUserAndGetPersonalnummer()).thenReturn(personalnummer);

        var result = performRequest(dtoInvalidSinceNoStartAndEndDates)
                .andExpect(status().isOk())
                .andReturn();

        var responsePayload = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), PayloadResponse.class);
        assertFalse(responsePayload.isSuccess());
    }

    @Test
    @SneakyThrows
    void validationsPass_createUrlaubRequest_returnAndPersistAsExpected() {

        when(environmentServiceMock.checkLoggedInUserAndGetPersonalnummer()).thenReturn(personalnummer);
        mockDtoValidationsAsPassing();
        var request = getValidDto();


        var result = performRequest(request)
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), AbwesenheitV2Dto.class);
        assertThat(responseBody).satisfies(a -> {
            assertThat(a.getStartDate()).isEqualTo(request.getStartDate());
            assertThat(a.getEndDate()).isEqualTo(request.getEndDate());
            assertThat(a.getType()).isEqualTo(request.getType());
            assertThat(a.getComment()).isEqualTo(request.getComment());
        });
        assertThat(abwesenheitRespository.findAll())
                .singleElement()
                .satisfies(a -> {
                    assertThat(a.getId()).isEqualTo(1L);
                    assertThat(a.getKommentar()).isEqualTo(request.getComment());
                    assertThat(a.getVon()).isEqualTo(request.getStartDate());
                    assertThat(a.getBis()).isEqualTo(request.getEndDate());
                    assertThat(a.getStatus()).isEqualTo(AbwesenheitStatus.VALID);
                    assertThat(a.getPersonalnummer()).isEqualTo(personalnummer);
                    assertThat(a.getTyp()).isEqualTo(request.getType().getValue());
                    assertThat(a.getCreatedOn()).isNotNull();
                    assertThat(a.getChangedOn()).isNotNull();
                    assertThat(a.getCreatedBy()).isEqualTo(GlobalUserHolder.IBOSNG_BACKEND);
                    assertThat(a.getChangedBy()).isEqualTo(GlobalUserHolder.IBOSNG_BACKEND);
                    assertThat(a.getGrund()).isEqualTo("URLAU");
                    assertThat(a.getFuehrungskraefte()).hasSize(1);
                    assertThat(a.getBeschreibung()).isNull();
                    assertThat(a.getArt()).isNull();
                    assertThat(a.getCommentFuehrungskraft()).isNull();
                    assertThat(a.getTage()).isNull();
                    assertThat(a.getSaldo()).isNull();
                    assertThat(a.getVerbaucht()).isNull();
                    assertThat(a.getIdLhr()).isNull();
                    assertThat(a.getLhrHttpStatus()).isNull();
                });
        assertThat(lhrOutboxEntryRepository.findAll())
                .singleElement()
                .satisfies(e -> {
                    assertThat(e.getId()).isEqualTo(1L);
                    assertThat(e.getOperation()).isEqualTo(CREATE_ABWESENHEIT_REQUEST);
                    assertThat(e.getStatus()).isIn(NEW, DONE);
                    assertThat(e.getData()).isEqualTo(Map.of("entityId", "1"));
                    assertThat(e.getCreatedAt()).isNotNull();
                    assertThat(e.getSyncedAt()).isNull();
                    assertThat(e.getErrorMessage()).isNull();
                });

    }

    @Test
    @SneakyThrows
    void persistingLhrOutboxEntryFails_createUrlaubRequest_ibosngPersistsRolledBack() {

        alterOutboxTableToForceErrorOnInsert();

        when(environmentServiceMock.checkLoggedInUserAndGetPersonalnummer()).thenReturn(personalnummer);
        mockDtoValidationsAsPassing();

        performRequest(getValidDto())
                .andExpect(status().isInternalServerError())
                .andReturn();

        assertThat(abwesenheitRespository.findAll()).isEmpty();

        restoreOutboxTable();
    }

    @SneakyThrows
    private void alterOutboxTableToForceErrorOnInsert() {
        try (var c = dataSource.getConnection();
             var st = c.createStatement()) {
            c.setAutoCommit(false);
            st.execute("ALTER TABLE lhr_outbox DROP COLUMN operation");
            c.commit();
        }
    }

    @SneakyThrows
    private void restoreOutboxTable() {
        try (var c = dataSource.getConnection();
             var st = c.createStatement()) {
            c.setAutoCommit(false);
            st.execute("ALTER TABLE lhr_outbox ADD COLUMN operation text not null");
            c.commit();
        }
    }

    private void mockDtoValidationsAsPassing() {
        when(leistungserfassungService.isLeistungserfassungMonthClosed(anyInt(), anyInt(), any(LocalDate.class))).thenReturn(false);
        when(zeitbuchungService.findZeitbuchungenInPeriodAndAnAbwesenheit(anyInt(), any(LocalDate.class), any(LocalDate.class), eq(Boolean.TRUE))).thenReturn(List.of());
        when(zeitausgleichService.findByPersonalnummerInPeriod(anyInt(), any(LocalDate.class), any(LocalDate.class), anyList())).thenReturn(List.of());
        when(abwesenheitService.findAbwesenheitBetweenDatesAndStatuses(anyInt(), any(LocalDate.class), any(LocalDate.class), anyList())).thenReturn(List.of());
    }

    private static @NotNull AbwesenheitCreateV2Dto getValidDto() {
        var dto = new AbwesenheitCreateV2Dto();
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(LocalDate.now().plusDays(1));
        dto.setComment("Ich haette gerne Urlaub");
        dto.setType(AbwesenheitType.URLAU);

        return dto;
    }

    private @NotNull Personalnummer persistAndGetCompletePersonalnummerWithBenutzer(String benutzerUpn) {

        var firma = IbisFirmaFactory.create();
        firma = ibisFirmaRepository.save(firma);

        var p = PersonalnummerFactory.createIn(firma);
        p = personalnummerRepository.save(p);

        var benutzer = BenutzerFactory.createFor(p, benutzerUpn);
        benutzerRepository.save(benutzer);

        return p;
    }

    @SneakyThrows
    private @NotNull ResultActions performRequest(AbwesenheitCreateV2Dto dto) {
        return mockMvc.perform(
                post("/v2/abwesenheiten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(dto))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer some token")
                        .accept(MediaType.APPLICATION_JSON)
        );

    }
}
