package com.ibosng.personalverwaltung;


import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.gatewayservice.services.impl.ZeiterfassungGatewayServiceImpl;
import com.ibosng.personalverwaltung.domain.AbwesenheitCreateV2;
import com.ibosng.personalverwaltung.domain.AbwesenheitMapperImpl;
import com.ibosng.personalverwaltung.domain.AbwesenheitenV2Service;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntryRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Operation.CREATE_ABWESENHEIT_REQUEST;
import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Status.NEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbwesenheitenV2ServiceTest {

    @Mock
    private AbwesenheitRespository abwesenheitRepositoryMock;

    @Mock
    private LhrOutboxEntryRepository lhrOutboxEntryRepositoryMock;

    @Mock
    private ZeiterfassungGatewayServiceImpl zeiterfassungGatewayServiceMock;

    private AbwesenheitenV2Service sut;

    @BeforeEach
    void setUp() {
        sut = new AbwesenheitenV2Service(abwesenheitRepositoryMock, lhrOutboxEntryRepositoryMock, new AbwesenheitMapperImpl(), zeiterfassungGatewayServiceMock);
    }

    @Test
    @SneakyThrows
    public void persistsAbwesenheitAndLhrOutboxEntry() {

        var request = getRequest();
        when(zeiterfassungGatewayServiceMock.getFuehrungskraftFor(request.getPersonalnummer()))
                .thenReturn(new Benutzer());
        when(abwesenheitRepositoryMock.save(any(Abwesenheit.class)))
                .thenReturn(getMockPersistedAbwesenheit());

        sut.createAbwesenheit(request);

        var abwesenheitArgumentCaptor = ArgumentCaptor.forClass(Abwesenheit.class);
        verify(abwesenheitRepositoryMock, times(1)).save(abwesenheitArgumentCaptor.capture());
        assertThat(abwesenheitArgumentCaptor.getValue()).satisfies(a -> {
            assertThat(a.getKommentar()).isEqualTo(request.getComment());
            assertThat(a.getVon()).isEqualTo(request.getStartDate());
            assertThat(a.getBis()).isEqualTo(request.getEndDate());
            assertThat(a.getStatus()).isEqualTo(AbwesenheitStatus.VALID);
            assertThat(a.getPersonalnummer()).isEqualTo(request.getPersonalnummer());
            assertThat(a.getTyp()).isEqualTo(request.getType().getValue());
            assertThat(a.getCreatedOn()).isNotNull();
            assertThat(a.getChangedOn()).isNotNull();
            assertThat(a.getCreatedBy()).isEqualTo(GlobalUserHolder.IBOSNG_BACKEND);
            assertThat(a.getChangedBy()).isEqualTo(GlobalUserHolder.IBOSNG_BACKEND);
            assertThat(a.getGrund()).isEqualTo("URLAU");
            assertThat(a.getFuehrungskraefte()).hasSize(1);
            assertThat(a.getId()).isNull();
            assertThat(a.getBeschreibung()).isNull();
            assertThat(a.getArt()).isNull();
            assertThat(a.getCommentFuehrungskraft()).isNull();
            assertThat(a.getTage()).isNull();
            assertThat(a.getSaldo()).isNull();
            assertThat(a.getVerbaucht()).isNull();
            assertThat(a.getIdLhr()).isNull();
            assertThat(a.getLhrHttpStatus()).isNull();
        });

        var outboxEntryCaptor = ArgumentCaptor.forClass(LhrOutboxEntry.class);
        verify(lhrOutboxEntryRepositoryMock, times(1)).save(outboxEntryCaptor.capture());
        assertThat(outboxEntryCaptor.getValue()).satisfies(e -> {
            assertThat(e.getId()).isNull();
            assertThat(e.getOperation()).isEqualTo(CREATE_ABWESENHEIT_REQUEST);
            assertThat(e.getStatus()).isEqualTo(NEW);
            assertThat(e.getData()).isEqualTo(Map.of("entityId", getMockPersistedAbwesenheit().getId().toString()));
            assertThat(e.getCreatedAt()).isNull();
            assertThat(e.getSyncedAt()).isNull();
            assertThat(e.getErrorMessage()).isNull();
        });

    }

    private static Abwesenheit getMockPersistedAbwesenheit() {
        var mockedPersistedAbwesenheit = new Abwesenheit();
        mockedPersistedAbwesenheit.setId(123);
        mockedPersistedAbwesenheit.setTyp(AbwesenheitType.URLAU.getValue());
        return mockedPersistedAbwesenheit;
    }

    private AbwesenheitCreateV2 getRequest() {
        return new AbwesenheitCreateV2(
                new Personalnummer(),
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                AbwesenheitType.URLAU,
                "Kommentar"
        );
    }

}
