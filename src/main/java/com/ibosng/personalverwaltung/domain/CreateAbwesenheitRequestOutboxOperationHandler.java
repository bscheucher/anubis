package com.ibosng.personalverwaltung.domain;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import com.ibosng.lhrservice.dtos.DnEintritteDto;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import com.ibosng.lhrservice.dtos.variabledaten.ZeitangabeDto;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.personalverwaltung.domain.exceptions.AbwesenheitCreationEmailException;
import com.ibosng.personalverwaltung.domain.exceptions.LhrOutboxProcessingException;
import com.ibosng.personalverwaltung.domain.exceptions.LhrResponseException;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.ibosng.gatewayservice.utils.Constants.ABWESENHEIT_GENEHMIGEN_LINK;
import static com.ibosng.lhrservice.utils.Constants.TERMINAL;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Slf4j
@Service
@RequiredArgsConstructor
public final class CreateAbwesenheitRequestOutboxOperationHandler extends OutboxOperationHandler {

    private final AbwesenheitRespository abwesenheitRepository;
    private final LHREnvironmentService lhrEnvironmentService;
    private final LHRClient lhrClient;
    private final MailService mailService;
    private final BenutzerService benutzerService;

    @Value("${nextAuthUrl:#{null}}")
    private String nextAuthUrl;

    @Override
    public LhrOutboxEntry.Operation supports() {
        return LhrOutboxEntry.Operation.CREATE_ABWESENHEIT_REQUEST;
    }

    @Override
    public void handle(LhrOutboxEntry entry) {
        var idSavedInOutbox = (String) entry.getData().get("entityId");

        var abwesenheit = abwesenheitRepository.findById(Integer.valueOf(idSavedInOutbox))
                .orElseThrow(() -> LhrOutboxProcessingException.fromEntityNotFound(entry.getId(), "Abwesenheit", idSavedInOutbox));

        tryCreateAbwesenheitInLhrOrSetAsInvalidAndThrow(abwesenheit, entry.getId());

        try {
            sendMailToFuehrungskraft(abwesenheit, entry.getId());
        } catch (AbwesenheitCreationEmailException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void tryCreateAbwesenheitInLhrOrSetAsInvalidAndThrow(Abwesenheit abwesenheit, Integer outboxEntryId) {
        try {
            createAbwesenheitInLhr(abwesenheit, outboxEntryId);
        } catch (LHRWebClientException e) {
            abwesenheitRepository.setStatusToInvalid(abwesenheit.getId());
            log.error("Exception occurred when calling LHR for creation of Abwesenheit {} for LhrOutboxEntry {}", abwesenheit.getId(), outboxEntryId, e);
            throw new LhrResponseException("Exception occurred when calling LHR for creation of Abwesenheit %d for LhrOutboxEntry %d".formatted(abwesenheit.getId(), outboxEntryId), e);
        }
    }

    /**
     * Follows the original msg-plaut logic of executing the lhrClient.postEintritt call (added null-checks and catch);
     */
    private void createAbwesenheitInLhr(Abwesenheit abwesenheit, Integer outboxEntryId) {
        var lhrKz = getFaKz(abwesenheit, outboxEntryId);
        var lhrNr = getFaNr(abwesenheit, outboxEntryId);
        var dnNr = getPersonalnummer(abwesenheit, outboxEntryId);
        var dnRef = DienstnehmerRefDto.builder()
                .faKz(lhrKz)
                .faNr(lhrNr)
                .dnNr(dnNr)
                .build();
        var eintritt = createEintrittDtoFrom(abwesenheit, outboxEntryId);
        var dnEintritte = new DnEintritteDto(dnRef, List.of(eintritt));

        lhrClient.postEintritt(lhrKz, lhrNr, dnNr, null, null, dnEintritte);
    }

    /**
     * Abwesenheit might be associated with multiple fuehrungskraefte. This implementation follows the original of just sending an email to the first entry.
     */
    private void sendMailToFuehrungskraft(Abwesenheit abwesenheit, Integer outboxEntryId) {
        var fuehrungskraftEmail = abwesenheit.getFuehrungskraefte().stream()
                .map(Benutzer::getEmail)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new AbwesenheitCreationEmailException("Handle create-abwesenheit request: No Fuehrungskraft or no email available for Fuehrungskraft for Abwesenheit %d (LhrOutboxEntry %d), therefore mail won't be sent".formatted(abwesenheit.getId(), outboxEntryId)));

        var fuehrungskraft = benutzerService.findByEmail(fuehrungskraftEmail);
        if (fuehrungskraft == null) {
            throw new AbwesenheitCreationEmailException("Handle create-abwesenheit request: Fuehrungskraft Benutzer not found by email %s for Abwesenheit %d (LhrOutboxEntry %d), therefore mail won't be sent".formatted(fuehrungskraftEmail, abwesenheit.getId(), outboxEntryId));
        }

        var mitarbeiter = benutzerService.findByPersonalnummer(abwesenheit.getPersonalnummer());
        if (mitarbeiter == null) {
            throw new AbwesenheitCreationEmailException("Handle create-abwesenheit request: Mitarbeiter Benutzer not found for Personalnummer %s for Abwesenheit %d (LhrOutboxEntry %d), therefore mail won't be sent".formatted(abwesenheit.getPersonalnummer().getPersonalnummer(), abwesenheit.getId(), outboxEntryId));
        }

        mailService.sendEmail(
                "gateway-service.ma-abwesenheit-info",
                "german",
                null,
                new String[]{fuehrungskraftEmail},
                toObjectArray(mitarbeiter.getFullName()),
                toObjectArray(fuehrungskraft.getFullName(), mitarbeiter.getFullName(), abwesenheit.getVon(), abwesenheit.getBis(),
                        ABWESENHEIT_GENEHMIGEN_LINK.formatted(nextAuthUrl))
        );

        log.info("Handle create-abwesenheit request: Email sent to Fuehrungskraft for Abwesenheit {} (LhrOutboxEntry {})", abwesenheit.getId(), outboxEntryId);
    }

    private String getFaKz(Abwesenheit abwesenheit, Integer outboxEntryId) {
        var faKz = lhrEnvironmentService.getFaKz(abwesenheit.getPersonalnummer().getFirma());
        if (faKz == null) {
            throwSinceEssentialPropertyNull(outboxEntryId, abwesenheit.getId(), "personalnummer.firma.lhrKz");
        }
        return faKz;
    }

    private Integer getFaNr(Abwesenheit abwesenheit, Integer outboxEntryId) {
        var faNr = lhrEnvironmentService.getFaNr(abwesenheit.getPersonalnummer().getFirma());
        if (faNr == null) {
            throwSinceEssentialPropertyNull(outboxEntryId, abwesenheit.getId(), "personalnummer.firma.lhrNr");
        }
        return faNr;
    }

    private Integer getPersonalnummer(Abwesenheit abwesenheit, Integer outboxEntryId) {
        var personalnummer = abwesenheit.getPersonalnummer().getPersonalnummer();
        if (personalnummer == null) {
            throwSinceEssentialPropertyNull(outboxEntryId, abwesenheit.getId(), "personalnummer.personalnummer");
        }
        return Integer.valueOf(abwesenheit.getPersonalnummer().getPersonalnummer());
    }

    private EintrittDto createEintrittDtoFrom(Abwesenheit abwesenheit, Integer outboxEntryId) {

        var startDate = abwesenheit.getVon();
        if (startDate == null) {
            throwSinceEssentialPropertyNull(outboxEntryId, abwesenheit.getId(), "von");
        }
        var endDate = abwesenheit.getBis();
        if (endDate == null) {
            throwSinceEssentialPropertyNull(outboxEntryId, abwesenheit.getId(), "bis");
        }

        return EintrittDto.builder()
                .grund(AbwesenheitType.fromValue(abwesenheit.getTyp()).toString())
                .kommentar(abwesenheit.getKommentar())
                .zeitangabe(new ZeitangabeDto(
                        startDate.format(DateTimeFormatter.ISO_DATE),
                        endDate.format(DateTimeFormatter.ISO_DATE)
                ))
                .source(TERMINAL)
                .build();
    }

    private void throwSinceEssentialPropertyNull(Integer outboxEntryId, Integer abwesenheitId, String nullFieldName) {
        throw LhrOutboxProcessingException.fromEssentialPropertyNull(outboxEntryId, "Abwesenheit", String.valueOf(abwesenheitId), nullFieldName);
    }
}
