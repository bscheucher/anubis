package com.ibosng.personalverwaltung;


import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.personalverwaltung.domain.CreateAbwesenheitRequestOutboxOperationHandler;
import com.ibosng.personalverwaltung.domain.exceptions.LhrOutboxProcessingException;
import com.ibosng.personalverwaltung.domain.exceptions.LhrResponseException;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import com.ibosng.personalverwaltung.utils.BenutzerFactory;
import com.ibosng.personalverwaltung.utils.PersonalnummerFactory;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAbwesenheitRequestOutboxOperationHandlerTest {

    @Mock
    private AbwesenheitRespository abwesenheitRepositoryMock;

    @Mock
    private LHREnvironmentService lhrEnvironmentServiceMock;

    @Mock
    private LHRClient lhrClientMock;

    @Mock
    private MailService mailServiceMock;

    @Mock
    private BenutzerService benutzerServiceMock;

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

    @Test
    @SneakyThrows
    public void lhrPostFails_throwLhrResponseExceptionAndUpdateAbwesenheitStatus() {

        var abwesenheit = getValidAbwesenheit();
        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(abwesenheit.getPersonalnummer().getFirma())).thenReturn("abc");
        when(lhrEnvironmentServiceMock.getFaNr(abwesenheit.getPersonalnummer().getFirma())).thenReturn(10);
        when(lhrClientMock.postEintritt(eq("abc"), eq(10), eq(123456789), isNull(), isNull(), any()))
                .thenThrow(mock(LHRWebClientException.class));

        var entry = getLhrOutboxEntry();
        assertThatThrownBy(() -> sut.handle(entry))
                .isInstanceOf(LhrResponseException.class)
                .hasMessage("Exception occurred when calling LHR for creation of Abwesenheit %d for LhrOutboxEntry %d".formatted(abwesenheit.getId(), entry.getId()));

        verify(abwesenheitRepositoryMock).setStatusToInvalid(abwesenheit.getId());
    }

    @Test
    @SneakyThrows
    public void successfulLhrPost_sendsEmailToFuehrungskraft() {

        var abwesenheit = getValidAbwesenheit();
        var mitarbeiter = getMitarbeiterBenutzerWithName(abwesenheit);
        var fuehrungskraft = getFuehrungskraftBenutzerWithName(abwesenheit);
        abwesenheit.getFuehrungskraefte().add(fuehrungskraft);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(abwesenheit.getPersonalnummer().getFirma())).thenReturn("abc");
        when(lhrEnvironmentServiceMock.getFaNr(abwesenheit.getPersonalnummer().getFirma())).thenReturn(10);
        when(benutzerServiceMock.findByEmail("manager@example.com")).thenReturn(fuehrungskraft);
        when(benutzerServiceMock.findByPersonalnummer(abwesenheit.getPersonalnummer())).thenReturn(mitarbeiter);

        sut.handle(getLhrOutboxEntry());

        var emailSubjectArgsCaptor = ArgumentCaptor.forClass(Object[].class);
        var emailBodyArgsCaptor = ArgumentCaptor.forClass(Object[].class);
        var recipientsCaptor = ArgumentCaptor.forClass(String[].class);

        verify(mailServiceMock).sendEmail(
                eq("gateway-service.ma-abwesenheit-info"),
                eq("german"),
                isNull(),
                recipientsCaptor.capture(),
                emailSubjectArgsCaptor.capture(),
                emailBodyArgsCaptor.capture()
        );

        assertThat(recipientsCaptor.getValue())
                .hasSize(1)
                .contains("manager@example.com");

        assertThat(emailSubjectArgsCaptor.getValue())
                .hasSize(1)
                .containsExactly("John Employee");

        assertThat(emailBodyArgsCaptor.getValue())
                .hasSize(5)
                .satisfies(args -> {
                    assertThat(args[0]).isEqualTo("Max Manager");
                    assertThat(args[1]).isEqualTo("John Employee");
                    assertThat(args[2]).isEqualTo(abwesenheit.getVon());
                    assertThat(args[3]).isEqualTo(abwesenheit.getBis());
                    assertThat(args[4]).asString().endsWith("/meine-mitarbeiter/abwesenheiten");
                });
    }

    @Test
    @SneakyThrows
    public void noFuehrungskraft_emailNotSent() {

        var abwesenheit = getValidAbwesenheit();

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(abwesenheit.getPersonalnummer().getFirma())).thenReturn("abc");
        when(lhrEnvironmentServiceMock.getFaNr(abwesenheit.getPersonalnummer().getFirma())).thenReturn(10);

        sut.handle(getLhrOutboxEntry());

        verify(mailServiceMock, never()).sendEmail(any(), any(), any(), any(), any(), any());
    }

    @Test
    @SneakyThrows
    public void fuehrungskraftNotFoundByEmail_emailNotSent() {

        var abwesenheit = getValidAbwesenheit();
        var fuehrungskraft = BenutzerFactory.createForEmail("manager@example.com");
        abwesenheit.getFuehrungskraefte().add(fuehrungskraft);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(abwesenheit.getPersonalnummer().getFirma())).thenReturn("abc");
        when(lhrEnvironmentServiceMock.getFaNr(abwesenheit.getPersonalnummer().getFirma())).thenReturn(10);
        when(benutzerServiceMock.findByEmail("manager@example.com")).thenReturn(null);

        sut.handle(getLhrOutboxEntry());

        verify(mailServiceMock, never()).sendEmail(any(), any(), any(), any(), any(), any());
    }

    @Test
    @SneakyThrows
    public void mitarbeiterNotFoundByPersonalnummer_emailNotSent() {

        var abwesenheit = getValidAbwesenheit();
        var fuehrungskraft = BenutzerFactory.createForEmail("manager@example.com");
        abwesenheit.getFuehrungskraefte().add(fuehrungskraft);

        when(abwesenheitRepositoryMock.findById(5555)).thenReturn(Optional.of(abwesenheit));
        when(lhrEnvironmentServiceMock.getFaKz(abwesenheit.getPersonalnummer().getFirma())).thenReturn("abc");
        when(lhrEnvironmentServiceMock.getFaNr(abwesenheit.getPersonalnummer().getFirma())).thenReturn(10);
        when(benutzerServiceMock.findByEmail("manager@example.com")).thenReturn(fuehrungskraft);
        when(benutzerServiceMock.findByPersonalnummer(abwesenheit.getPersonalnummer())).thenReturn(null);

        sut.handle(getLhrOutboxEntry());

        verify(mailServiceMock, never()).sendEmail(any(), any(), any(), any(), any(), any());
    }

    private static Abwesenheit getAbwesenheit() {
        var abwesenheit = new Abwesenheit();
        abwesenheit.setId(5555);
        return abwesenheit;
    }

    private static Abwesenheit getValidAbwesenheit() {
        var abwesenheit = getAbwesenheit();
        abwesenheit.setVon(LocalDate.now());
        abwesenheit.setBis(LocalDate.now().plusDays(1));
        abwesenheit.setTyp(AbwesenheitType.URLAU.getValue());
        var personalnummer = PersonalnummerFactory.createWithFirma();
        abwesenheit.setPersonalnummer(personalnummer);
        return abwesenheit;
    }

    private static LhrOutboxEntry getLhrOutboxEntry() {
        var entry = new LhrOutboxEntry();
        entry.setId(1234);
        entry.setData(Map.of("entityId", "5555"));
        return entry;
    }

    private static @NotNull Benutzer getFuehrungskraftBenutzerWithName(Abwesenheit abwesenheit) {
        var fuehrungskraft = BenutzerFactory.createForEmail("manager@example.com");
        fuehrungskraft.setFirstName("Max");
        fuehrungskraft.setLastName("Manager");
        abwesenheit.getFuehrungskraefte().add(fuehrungskraft);
        return fuehrungskraft;
    }

    private static @NotNull Benutzer getMitarbeiterBenutzerWithName(Abwesenheit abwesenheit) {
        var employee = BenutzerFactory.createFor(abwesenheit.getPersonalnummer(), "employee@example.com");
        employee.setFirstName("John");
        employee.setLastName("Employee");
        return employee;
    }

}