package com.ibosng.personalverwaltung.domain;

import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import com.ibosng.lhrservice.dtos.DnEintritteDto;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import com.ibosng.lhrservice.dtos.variabledaten.ZeitangabeDto;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.personalverwaltung.domain.exceptions.LhrOutboxProcessingException;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.ibosng.lhrservice.utils.Constants.TERMINAL;

@Service
@RequiredArgsConstructor
public final class CreateAbwesenheitRequestOutboxOperationHandler extends OutboxOperationHandler {

    private final AbwesenheitRespository abwesenheitRepository;
    private final LHREnvironmentService lhrEnvironmentService;
    private final LHRClient lhrClient;

    @Override
    public LhrOutboxEntry.Operation supports() {
        return LhrOutboxEntry.Operation.CREATE_ABWESENHEIT_REQUEST;
    }

    @Override
    public void handle(LhrOutboxEntry entry) {
        var idSavedInOutbox = (String) entry.getData().get("entityId");
        var abwesenheit = abwesenheitRepository.findById(Integer.valueOf(idSavedInOutbox))
                .orElseThrow(() -> LhrOutboxProcessingException.fromEntityNotFound(entry.getId(), "Abwesenheit", idSavedInOutbox));
        createAbwesenheitInLhr(abwesenheit, entry.getId());
    }

    // Contains unchanged msg-plaut logic of executing the lhrClient.postEintritt call (added null-checks);
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
                .grund(abwesenheit.getTyp())
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
