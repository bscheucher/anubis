package com.ibosng.personalverwaltung.web;

import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.personalverwaltung.domain.exceptions.AbwesenheitCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class AbwesenheitRequestValidator {

    private final LeistungserfassungService leistungserfassungService;
    private final ZeitbuchungService zeitbuchungService;
    private final AbwesenheitService abwesenheitService;
    private final ZeitausgleichService zeitausgleichService;


    /**
     *  Copies the originally existing validation for the request, merely throwing new custom exception.
     *  Exceptions are created with OK status-code so that these validation-messages are shown to the user (with current FE request processing logic)
     */
    public void throwIfAbwesenheitRequestInvalid(AbwesenheitCreateV2Dto dto, Personalnummer personalnummer) {


        if (dto.getStartDate() == null || dto.getEndDate() == null
                || leistungserfassungService.isLeistungserfassungMonthClosed(personalnummer.getId(), personalnummer.getFirma().getBmdClient(), dto.getStartDate())
                || leistungserfassungService.isLeistungserfassungMonthClosed(personalnummer.getId(), personalnummer.getFirma().getBmdClient(), dto.getEndDate())) {
            throw new AbwesenheitCreationException("Keine Buchung oder Änderung in abgeschlossenen Monat möglich", OK);
        }

        List<Zeitbuchung> zeitbuchungsOverlaps = zeitbuchungService.findZeitbuchungenInPeriodAndAnAbwesenheit(personalnummer.getId(), dto.getStartDate(), dto.getEndDate(), Boolean.TRUE);
        if (!zeitbuchungsOverlaps.isEmpty()) {
            throw new AbwesenheitCreationException("Für den angefragten Zeitraum gibt es Leistungsbuchungen, deshalb kann die Abwesenheitsanfrage nicht bearbeitet werden. Bitte wähle einen anderen Zeitraum aus und versuch es erneut.", OK);
        }

        final List<AbwesenheitStatus> abwStatuses = List.of(AbwesenheitStatus.VALID, AbwesenheitStatus.ACCEPTED, AbwesenheitStatus.ACCEPTED_FINAL, AbwesenheitStatus.USED);
        final List<Zeitausgleich> zeitausgleichList = zeitausgleichService.findByPersonalnummerInPeriod(personalnummer.getId(), dto.getStartDate(), dto.getEndDate(), abwStatuses);
        if (!zeitausgleichList.isEmpty()) {
            throw new AbwesenheitCreationException("Für diesen Zeitraum existiert bereits eine Abwesenheit. Bitte wähle einen anderen Zeitraum.", OK);
        }

        final List<Abwesenheit> abwesenheitList = abwesenheitService.findAbwesenheitBetweenDatesAndStatuses(personalnummer.getId(), dto.getStartDate(), dto.getEndDate(), abwStatuses);
        if (!abwesenheitList.isEmpty()) {
            throw new AbwesenheitCreationException("Für diesen Zeitraum existiert bereits eine Abwesenheit. Bitte wähle einen anderen Zeitraum.", OK);
        }

    }
}
