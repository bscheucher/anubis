package com.ibosng.personalverwaltung;


import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.personalverwaltung.domain.CreateAbwesenheitRequestOutboxOperationHandler;
import com.ibosng.personalverwaltung.domain.exceptions.LhrOutboxProcessingException;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import com.ibosng.personalverwaltung.utils.PersonalnummerFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateAbwesenheitRequestOutboxOperationHandlerTest {

    @Mock
    private AbwesenheitRespository abwesenheitRepositoryMock;

    @Mock
    private LHREnvironmentService lhrEnvironmentServiceMock;

    @InjectMocks
    private CreateAbwesenheitRequestOutboxOperationHandler sut;

    @Test
    @SneakyThrows
    public void abwesenheitNotFound_throw() {

        var entry = getLhrOutboxEntry();

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.handle(entry))
                .isInstanceOf(LhrOutboxProcessingException.class)
                .hasMessage("Could not process LhrOutboxEntry %d : The associated entity of type Abwesenheit with id %s was not found."
                        .formatted(entry.getId(), entry.getData().get("entityId")));
    }

    @Test
    @SneakyThrows
    public void lhrKzNull_throw() {

        var entry = getLhrOutboxEntry();

        var abwesenheit = getAbwesenheit();
        var personalnummer = PersonalnummerFactory.createWithFirma();
        abwesenheit.setPersonalnummer(personalnummer);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(personalnummer.getFirma())).thenReturn(null);

        assertThatThrownBy(() -> sut.handle(entry))
                .isInstanceOf(LhrOutboxProcessingException.class)
                .hasMessage("Could not process LhrOutboxEntry %d : The associated entity of type Abwesenheit with id %s contains a null-field essential for calling LHR: personalnummer.firma.lhrKz."
                        .formatted(entry.getId(), entry.getData().get("entityId")));
    }


    @Test
    @SneakyThrows
    public void lhrNrNull_throw() {

        var entry = getLhrOutboxEntry();

        var abwesenheit = getAbwesenheit();
        var personalnummer = PersonalnummerFactory.createWithFirma();
        abwesenheit.setPersonalnummer(personalnummer);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(personalnummer.getFirma())).thenReturn("irrelevant");
        when(lhrEnvironmentServiceMock.getFaNr(personalnummer.getFirma())).thenReturn(null);

        assertThatThrownBy(() -> sut.handle(entry))
                .isInstanceOf(LhrOutboxProcessingException.class)
                .hasMessage("Could not process LhrOutboxEntry %d : The associated entity of type Abwesenheit with id %s contains a null-field essential for calling LHR: personalnummer.firma.lhrNr."
                        .formatted(entry.getId(), entry.getData().get("entityId")));
    }

    @Test
    @SneakyThrows
    public void personalnummerNull_throw() {

        var entry = getLhrOutboxEntry();

        var abwesenheit = getAbwesenheit();
        var personalnummer = PersonalnummerFactory.createWithFirma();
        personalnummer.setPersonalnummer(null);
        abwesenheit.setPersonalnummer(personalnummer);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(personalnummer.getFirma())).thenReturn("irrelevant");
        when(lhrEnvironmentServiceMock.getFaNr(personalnummer.getFirma())).thenReturn(123);

        assertThatThrownBy(() -> sut.handle(entry))
                .isInstanceOf(LhrOutboxProcessingException.class)
                .hasMessage("Could not process LhrOutboxEntry %d : The associated entity of type Abwesenheit with id %s contains a null-field essential for calling LHR: personalnummer.personalnummer."
                        .formatted(entry.getId(), entry.getData().get("entityId")));
    }


    @Test
    @SneakyThrows
    public void vonNull_throw() {

        var entry = getLhrOutboxEntry();

        var abwesenheit = getAbwesenheit();
        var personalnummer = PersonalnummerFactory.createWithFirma();
        abwesenheit.setPersonalnummer(personalnummer);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(personalnummer.getFirma())).thenReturn("irrelevant");
        when(lhrEnvironmentServiceMock.getFaNr(personalnummer.getFirma())).thenReturn(123);

        assertThatThrownBy(() -> sut.handle(entry))
                .isInstanceOf(LhrOutboxProcessingException.class)
                .hasMessage("Could not process LhrOutboxEntry %d : The associated entity of type Abwesenheit with id %s contains a null-field essential for calling LHR: von."
                        .formatted(entry.getId(), entry.getData().get("entityId")));
    }

    @Test
    @SneakyThrows
    public void bisNull_throw() {

        var entry = getLhrOutboxEntry();

        var abwesenheit = getAbwesenheit();
        abwesenheit.setVon(LocalDate.now());
        var personalnummer = PersonalnummerFactory.createWithFirma();
        abwesenheit.setPersonalnummer(personalnummer);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(personalnummer.getFirma())).thenReturn("irrelevant");
        when(lhrEnvironmentServiceMock.getFaNr(personalnummer.getFirma())).thenReturn(123);

        assertThatThrownBy(() -> sut.handle(entry))
                .isInstanceOf(LhrOutboxProcessingException.class)
                .hasMessage("Could not process LhrOutboxEntry %d : The associated entity of type Abwesenheit with id %s contains a null-field essential for calling LHR: bis."
                        .formatted(entry.getId(), entry.getData().get("entityId")));
    }

    private static LhrOutboxEntry getLhrOutboxEntry() {
        var entry = new LhrOutboxEntry();
        entry.setId(1234);
        entry.setData(Map.of("entityId", "5555"));
        return entry;
    }

    private static Abwesenheit getAbwesenheit() {
        var abwesenheit = new Abwesenheit();
        abwesenheit.setId(5555);
        return abwesenheit;
    }

}