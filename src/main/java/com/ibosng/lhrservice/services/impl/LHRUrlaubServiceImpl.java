package com.ibosng.lhrservice.services.impl;

import com.ibosng._service.AsyncService;
import com.ibosng.dbservice.dtos.UrlaubsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungstyp;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchungstyp;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.urlaub.UrlaubsdatenService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungstypService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.*;
import com.ibosng.lhrservice.dtos.urlaube.UrlaubswertDailyDaysDto;
import com.ibosng.lhrservice.dtos.urlaube.UrlaubswertDailyDto;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import com.ibosng.lhrservice.dtos.variabledaten.ZeitangabeDto;
import com.ibosng.lhrservice.dtos.zeitdaten.AnfrageSuccessDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.exceptions.TechnicalException;
import com.ibosng.lhrservice.services.*;
import com.ibosng.microsoftgraphservice.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.lhrservice.utils.Constants.*;
import static com.ibosng.lhrservice.utils.Helpers.findKeyByValue;
import static com.ibosng.lhrservice.utils.Helpers.handleLHRWebClientException;
import static com.ibosng.lhrservice.utils.Parsers.mergeLocalDateTime;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Slf4j
@Service
@RequiredArgsConstructor
public class LHRUrlaubServiceImpl implements LHRUrlaubService {
    private final LHRClient lhrClient;
    private final PersonalnummerService personalnummerService;
    private final LHREnvironmentService lhrEnvironmentService;
    private final LHRMapperService lhrMapperService;
    private final AbwesenheitService abwesenheitService;
    private final LHRDienstnehmerService dienstnehmerService;
    private final LeistungserfassungService leistungserfassungService;
    private final ZeitbuchungService zeitbuchungService;
    private final ZeitausgleichService zeitausgleichService;
    private final UrlaubsdatenService urlaubsdatenService;
    private final ZeitbuchungstypService zeitbuchungstypService;
    private final HelperService helperService;
    private final StammdatenService stammdatenService;
    private final MailService mailService;
    private final BenutzerService benutzerService;
    private final AsyncService asyncService;

    @Override
    public ResponseEntity<?> getAllUrlaubstand(Integer personalnummerId, String effectiveDate) {
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (pn == null) {
            log.error("Personalnummer object not found for {} in getAllUrlaubstand", personalnummerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            return lhrClient.getEintritte(lhrEnvironmentService.getFaKz(pn.getFirma()), lhrEnvironmentService.getFaNr(pn.getFirma()), parseStringToInteger(pn.getPersonalnummer()), effectiveDate, new String[]{URLAUB_ART});
        } catch (LHRWebClientException e) {
            log.error("LHR client returned error for getting eintritte for personalnummer: {}", e.getMessage());
            return handleLHRWebClientException(e, pn.getPersonalnummer(), "getAllUrlaubstand");
        }
    }

    @Override
    public ResponseEntity<?> getAllUrlaubs(Integer personalnummerId, String fromDate, String endDate) {
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (pn == null) {
            log.error("Personalnummer object not found for {} in getAllUrlaubs", personalnummerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String firma = lhrEnvironmentService.getFaKz(pn.getFirma());
        Integer firmaNr = lhrEnvironmentService.getFaNr(pn.getFirma());
        try {
            ResponseEntity<UrlaubsdatenStandaloneDto> urlaunsdaten = lhrClient.getUrlaube(firma, firmaNr, parseStringToInteger(pn.getPersonalnummer()), fromDate, endDate);
            List<UrlaubsdatenDto> urlaubsdatens = lhrMapperService.mapUrlaubsdatenAndUpdate(urlaunsdaten.getBody(), pn).stream()
                    .map(urlaubsdatenService::mapToDto).toList();
            return ResponseEntity.ok(urlaubsdatens);
        } catch (LHRWebClientException e) {
            log.error("LHR client returned error for getting eintritte for personalnummer: {}", e.getMessage());
            return handleLHRWebClientException(e, pn.getPersonalnummer(), "getAllUrlaubs");
        }
    }

    @Override
    public ResponseEntity<?> getUrlaubstand(Integer personalnummerId, String date) {
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (pn == null) {
            log.error("Personalnummer object not found for {} in getUrlaubstand", personalnummerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            return lhrClient.getEintritt(lhrEnvironmentService.getFaKz(pn.getFirma()), lhrEnvironmentService.getFaNr(pn.getFirma()), parseStringToInteger(pn.getPersonalnummer()), URLAUB_GRUND, date);
        } catch (LHRWebClientException e) {
            log.error("LHR client returned error for getting eintritt for personalnummer: {}", e.getMessage());
            return handleLHRWebClientException(e, pn.getPersonalnummer(), "getUrlaubstand");
        }
    }

    @Override
    public ResponseEntity<?> createUrlaub(String modify, String ignore, AbwesenheitDto abwesenheitDto) {
        if (abwesenheitDto.getPersonalnummerId() == null) {
            log.error("Personalnummer object not found for {} in createUrlaub", abwesenheitDto.getPersonalnummerId());
            return ResponseEntity.notFound().build();
        }

        Personalnummer personalnummer = personalnummerService.findById(abwesenheitDto.getPersonalnummerId()).orElse(null);
        if (personalnummer == null || personalnummer.getFirma() == null) {
            log.error("IbisFirma is null for personalnummer {}", personalnummer != null ? personalnummer.getPersonalnummer() : null);
            return ResponseEntity.notFound().build();
        }

        final int dnNr = parseStringToInteger(personalnummer.getPersonalnummer());
        ResponseEntity<DnStammStandaloneDto> existingDnStammStandaloneDtoResponse = dienstnehmerService.getDienstnehmerstamm(personalnummer.getId());

        if (existingDnStammStandaloneDtoResponse == null || existingDnStammStandaloneDtoResponse.getBody() == null || existingDnStammStandaloneDtoResponse.getBody().getDienstnehmerstamm() == null) {
            if (!lhrEnvironmentService.isProduction()) {
                dienstnehmerService.createOrUpdateLHRDienstnehmerstamm(personalnummer.getId());
            } else {
                log.error("NO data in LHR for personalnummer {}", abwesenheitDto.getPersonalnummerId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("NO data in LHR for personalnummer %s", abwesenheitDto.getPersonalnummerId()));
            }
        }

        final String firma = lhrEnvironmentService.getFaKz(personalnummer.getFirma());
        final Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummer.getFirma());

        EintrittDto eintritt = EintrittDto.builder()
                .grund(abwesenheitDto.getType().toString())
                .kommentar(abwesenheitDto.getComment())
                .zeitangabe(new ZeitangabeDto(
                        abwesenheitDto.getStartDate().format(DateTimeFormatter.ISO_DATE),
                        abwesenheitDto.getEndDate().format(DateTimeFormatter.ISO_DATE)
                ))
                .source(TERMINAL)
                .build();

        DienstnehmerRefDto dnRef = DienstnehmerRefDto.builder().faKz(firma).faNr(firmaNr).dnNr(dnNr).build();
        DnEintritteDto dnEintritte = new DnEintritteDto(dnRef, List.of(eintritt));

        try {
            if (abwesenheitDto.getId() != null) {
                Abwesenheit abwesenheit = abwesenheitService.findById(abwesenheitDto.getId()).orElse(null);
                if (abwesenheit == null || !AbwesenheitStatus.INVALID.equals(abwesenheit.getStatus())) {
                    log.warn("Wrong status/abwesenheiten not found");
                    return ResponseEntity.notFound().build();
                }
            }

            ResponseEntity<DnEintritteDto> response = lhrClient.postEintritt(firma, firmaNr, parseStringToInteger(personalnummer.getPersonalnummer()), modify, ignore, dnEintritte);

            DnEintrittDto savedEintr = lhrClient.getEintritt(firma, firmaNr, parseStringToInteger(personalnummer.getPersonalnummer()),
                    abwesenheitDto.getType().toString(), abwesenheitDto.getStartDate().format(DateTimeFormatter.ISO_DATE)).getBody();

            if (response.getStatusCode().is4xxClientError()) { //we should not rely on catch block
                return ResponseEntity.status(409).build();
            }

            if (abwesenheitDto.getId() == null) {
                Abwesenheit toSave = abwesenheitService.map(abwesenheitDto);
                toSave.setStatus(AbwesenheitStatus.NEW);
                toSave.setCreatedBy(LHR_SERVICE);
                abwesenheitDto.setId(abwesenheitService.save(toSave).getId());
            }

            AbwesenheitDto result = lhrMapperService.updateAbwesenheit(savedEintr, abwesenheitDto.getId(),
                    response.getStatusCode().is2xxSuccessful() ? AbwesenheitStatus.VALID : AbwesenheitStatus.INVALID);

            abwesenheitService.findById(result.getId()).ifPresent(abw -> {
                saveKonsumForAbwesenheit(parseStringToInteger(personalnummer.getPersonalnummer()), firma, firmaNr, abw);
                abw.setLhrHttpStatus(response.getStatusCode().value());
                abw.setChangedOn(getLocalDateNow());
                abw.setChangedBy(LHR_SERVICE);
                abwesenheitService.save(abw);
            });

            return ResponseEntity.ok(result);
        } catch (LHRWebClientException e) {
            log.error("Error occured while submitting abwesenheit: {}", e.getMessage());
            if (abwesenheitDto.getId() != null) {
                abwesenheitService.findById(abwesenheitDto.getId()).ifPresent(abw -> {
                    abw.setStatus(AbwesenheitStatus.INVALID);
                    abw.setLhrHttpStatus(e.getHttpStatus().value());
                    abwesenheitService.save(abw);
                });
            }
            if (e.getHttpStatus().is4xxClientError()) {
                return ResponseEntity.status(409).build();
            }
            return handleLHRWebClientException(e, personalnummer.getPersonalnummer(), "createUrlaub");
        }
    }

    @Override
    public ResponseEntity<?> deleteUrlaub(AbwesenheitDto abwesenheitDto, String modify, String ignore) {
        Personalnummer personalnummer = personalnummerService.findById(abwesenheitDto.getPersonalnummerId()).orElse(null);
        String firma;
        Integer firmaNr;
        if (personalnummer != null && personalnummer.getFirma() != null) {
            firma = lhrEnvironmentService.getFaKz(personalnummer.getFirma());
            firmaNr = lhrEnvironmentService.getFaNr(personalnummer.getFirma());
        } else {
            log.error("IbisFirma is null for personalnummer {}", personalnummer.getPersonalnummer());
            return null;
        }
        Abwesenheit abwesenheit = abwesenheitService.findById(abwesenheitDto.getId()).orElse(null);
        ResponseEntity<DnEintrittDto> response;
        try {
            response = lhrClient.deleteEintritt(firma, firmaNr, parseStringToInteger(personalnummer.getPersonalnummer()),
                    abwesenheitDto.getType().toString(), abwesenheitDto.getStartDate().format(DateTimeFormatter.ISO_DATE), modify, ignore);
        } catch (LHRWebClientException ex) {
            if (abwesenheit != null) {
                abwesenheit.setStatus(AbwesenheitStatus.ERROR);
                abwesenheitService.save(abwesenheit);
            }
            return handleLHRWebClientException(ex, personalnummer.getPersonalnummer(), "deleteUrlaub");
        }

        if (response != null && response.getStatusCode().is2xxSuccessful()) {

            if (abwesenheit != null) {
                abwesenheit.setStatus(AbwesenheitStatus.CANCELED);
                abwesenheitService.save(abwesenheit);

                asyncService.asyncExecutor(() -> syncUrlaubDetails(abwesenheitDto.getPersonalnummerId(), abwesenheitDto.getStartDate().toString(),
                        abwesenheitDto.getEndDate().toString()));

            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<?> sendLeistungsdatumToLhr(Integer personalnummerId, String leistungDatum) {
        List<Leistungserfassung> leistungserfassungen = leistungserfassungService.findByPersonalnummerAndDateAndLeistungstype(personalnummerId, leistungDatum, Leistungstyp.LEISTUNG);
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (leistungserfassungen == null || leistungserfassungen.isEmpty()) {
            log.error("No leistungserfassung found for pnId {} in sendLeistungsdatumToLhr", personalnummerId);
            return ResponseEntity.notFound().build();
        } else if (pn == null) {
            log.error("Personalnummer object not found for {} in sendLeistungsdatumToLhr", personalnummerId);
            return ResponseEntity.notFound().build();
        }

        String firma = lhrEnvironmentService.getFaKz(pn.getFirma());
        Integer firmaNr = lhrEnvironmentService.getFaNr(pn.getFirma());

        List<ResponseEntity<?>> responses = new ArrayList<>();
        for (Leistungserfassung leistungserfassung : leistungserfassungen) {
            if (Boolean.TRUE.equals(leistungserfassung.getIsSyncedWithLhr())) {
                log.warn("Leisterfassung for personalnummer {} and date {} already synced", pn.getPersonalnummer(), leistungDatum);
                continue;
            }
            leistungserfassung.setIsSyncedWithLhr(true);
            try {
                ResponseEntity<?> response = sendZeitbuchung(BUCHUNGANFRAGE_I, leistungserfassung, firma, firmaNr, parseStringToInteger(pn.getPersonalnummer()));
                responses.add(response);
            } catch (TechnicalException technicalException) {
                leistungserfassung.setIsSyncedWithLhr(false);
            }

            leistungserfassung.setChangedOn(getLocalDateNow());
            leistungserfassung.setChangedBy(LHR_SERVICE);
            leistungserfassungService.save(leistungserfassung);
        }
        // Check responses and determine what to return
        boolean allSuccess = responses.stream().allMatch(response -> response.getStatusCode().is2xxSuccessful());

        if (allSuccess) {
            return ResponseEntity.ok("All Leistungserfassungen processed successfully.");
        } else {
            List<String> errorDetails = responses.stream()
                    .filter(response -> !response.getStatusCode().is2xxSuccessful())
                    .map(response -> "Error: " + response.getStatusCode().toString())
                    .toList();

            return ResponseEntity.status(207).body("Some Leistungserfassungen failed: " + String.join(", ", errorDetails));
        }
    }

    @Override
    public ResponseEntity<?> deleteLeistungsdatumToLhr(Integer personalnummerId, String leistungDatum) {
        List<Leistungserfassung> leistungserfassungen = leistungserfassungService.findByPersonalnummerAndDateAndLeistungstype(personalnummerId, leistungDatum, Leistungstyp.LEISTUNG);
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);

        if (pn == null) {
            log.error("Personalnummer object not found for {} in deleteLeistungsdatumToLhr", personalnummerId);
            return ResponseEntity.notFound().build();
        } else if (leistungserfassungen == null || leistungserfassungen.isEmpty()) {
            log.error("No leistungserfassung found for pn {} in deleteLeistungsdatumToLhr", personalnummerId);
            return ResponseEntity.notFound().build();
        }

        List<ResponseEntity<?>> responses = new ArrayList<>();
        for (Leistungserfassung leistungserfassung : leistungserfassungen) {
            if (Boolean.FALSE.equals(leistungserfassung.getIsSyncedWithLhr())) {
                log.warn("Leistungserfassung for personalnummer ID {} and date {} not synced", personalnummerId, leistungDatum);
                responses.add(ResponseEntity.unprocessableEntity().body("Leistungserfassung not synced for date " + leistungDatum));
                continue;
            }
            String firma = lhrEnvironmentService.getFaKz(pn.getFirma());
            Integer firmaNr = lhrEnvironmentService.getFaNr(pn.getFirma());

            leistungserfassung.setIsSyncedWithLhr(false);
            try {
                ResponseEntity<?> response = sendZeitbuchung(BUCHUNGANFRAGE_D, leistungserfassung, firma, firmaNr, parseStringToInteger(pn.getPersonalnummer()));
                responses.add(response);
            } catch (TechnicalException technicalException) {
                leistungserfassung.setIsSyncedWithLhr(true);
            }

            leistungserfassung.setChangedOn(getLocalDateNow());
            leistungserfassung.setChangedBy(LHR_SERVICE);
            leistungserfassungService.save(leistungserfassung);
        }

        // Check responses and determine what to return
        boolean allSuccess = responses.stream().allMatch(response -> response.getStatusCode().is2xxSuccessful());
        if (allSuccess) {
            return ResponseEntity.ok("All Leistungserfassungen unsynced successfully.");
        } else {
            List<String> errorDetails = responses.stream()
                    .filter(response -> !response.getStatusCode().is2xxSuccessful())
                    .map(response -> "Error: " + response.getStatusCode().toString())
                    .toList();
            return ResponseEntity.status(207).body("Some Leistungserfassungen failed to unsync: " + String.join(", ", errorDetails));
        }
    }

    private void purgeExistingZA(Leistungserfassung leistungserfassung) {
        Personalnummer personalnummer = leistungserfassung.getPersonalnummer();
        List<Zeitausgleich> existing = zeitausgleichService
                .findByPersonalnummerAndDate(personalnummer.getId(), leistungserfassung.getLeistungsdatum())
                .stream()
                .flatMap(z -> zeitausgleichService.findAllZeitausgleichInPeriod(z.getId()).stream())
                .distinct()
                .toList();
        existing.forEach(za -> deleteZeitausgelich(personalnummer.getId(), za.getDatum().toString(), true));
        existing.forEach(za -> zeitausgleichService.deleteById(za.getId()));
    }

    private List<Zeitausgleich> fetchAllZeitausgleich(Zeitausgleich randomZa) {
        Zeitausgleich firstZeitausgleich = zeitausgleichService.findAllZeitausgleichInPeriod(randomZa.getId()).stream().min(Comparator.comparing(Zeitausgleich::getDatum)).orElse(null);
        if (firstZeitausgleich == null) {
            log.error("Unable to find first ZA for pn: {} date: {}", randomZa.getPersonalnummer().getPersonalnummer(), randomZa.getDatum());
            return List.of(randomZa);
        }

        List<Zeitausgleich> zeitausgleiches = zeitausgleichService.findByPersonalnummerInPeriod(randomZa.getPersonalnummer().getId(), firstZeitausgleich.getDatum(), firstZeitausgleich.getDatum().plusMonths(1));
        List<AbwesenheitDto> abwesenheitDtos = zeitausgleichService.mapListZeitausgleichToListAbwesenheitDto(zeitausgleiches);

        AbwesenheitDto abwesenheitDto = abwesenheitDtos.stream().filter(abw -> abw.getStartDate().isEqual(firstZeitausgleich.getDatum()) && AbwesenheitType.ZEITAUSGLEICH.equals(abw.getType()) && abw.getStatus().equals(randomZa.getStatus())).findFirst().orElse(null);

        Zeitausgleich zeitausgleich = zeitausgleichService.findByPersonalnummerAndDate(randomZa.getPersonalnummer().getId(), abwesenheitDto.getEndDate()).stream().filter(za -> randomZa.getStatus().equals(za.getStatus())).findFirst().orElse(null);
        if (zeitausgleich == null) {
            log.error("Unable to last ZA for pn: {} date: {}", randomZa.getPersonalnummer().getPersonalnummer(), randomZa.getDatum());
            return List.of(randomZa);
        }

        return zeitausgleichService.findAllZeitausgleichInPeriod(zeitausgleich.getId());
    }

    private ResponseEntity<?> sendZeitbuchung(final String buchungType, final Leistungserfassung leistungserfassung,
                                              final String firma, final Integer firmaNr, final Integer dnNr) throws TechnicalException {

        final Zeitbuchungstyp mitterspause = zeitbuchungstypService.findByType("Mittagspause");
        List<Zeitbuchung> zeitbuchungs = zeitbuchungService.getZeitbuchungenByListungserfassen(leistungserfassung);

        if (zeitbuchungs.isEmpty()) {
            log.info("No buchungen found for personalnummer {} and date {}", dnNr, leistungserfassung.getLeistungsdatum());
            return ResponseEntity.notFound().build();
        }

        purgeExistingZA(leistungserfassung);

        Map<String, List<String>> buchungsMap = prepareAndSquashBuchungAnfrage(zeitbuchungs);
        List<String> buchungsList = buchungsMap.values().stream().flatMap(Collection::stream).toList();
        List<String> buchunganfrageList = new LinkedList<>(buchungsList);

        //Manage abscences that need to be send as zeitbuchungen
        Map<Zeitbuchungstyp, List<Zeitbuchung>> mappedZeitbuchungenWieZeitausgleich = zeitbuchungs
                .stream()
                .filter(zeitbuchung ->
                        Boolean.FALSE.equals(zeitbuchung.getAnAbwesenheit()) &&
                                zeitbuchung.getZeitbuchungstyp() != null &&
                                Boolean.FALSE.equals(zeitbuchung.getZeitbuchungstyp().getIsLhrEintritt()) &&
                                !BUCHUNGANFRAGE_BILDUNGSFREISTELLUNG.equals(String.valueOf(zeitbuchung.getZeitbuchungstyp().getLhrZeitspeicher())) ||
                                (zeitbuchung.getZeitbuchungstyp() != null && mitterspause.getId().equals(zeitbuchung.getZeitbuchungstyp().getId()))
                )
                .collect(Collectors.groupingBy(Zeitbuchung::getZeitbuchungstyp));

        for (Map.Entry<Zeitbuchungstyp, List<Zeitbuchung>> entry : mappedZeitbuchungenWieZeitausgleich.entrySet()) {

            List<Zeitbuchung> zeitbuchungen = entry.getValue();
            String zeitspeicherValue = String.valueOf(entry.getKey().getLhrZeitspeicher());
            if (!zeitbuchungen.isEmpty()) {
                List<String> zeitausgleichList = prepareAndSquashZeitausgleich(zeitbuchungen);
                Queue<String> zeitausgleichQueue = new LinkedList<>(zeitausgleichList);

                while (!zeitausgleichQueue.isEmpty()) {
                    String zeitausgleichVon = zeitausgleichQueue.poll();
                    try {
                        AnfrageSuccessDto anfrageVon = lhrClient.postBuchunganfrage(firma, firmaNr, dnNr,
                                buchungType, zeitausgleichVon, ORIGINAL_JA, zeitspeicherValue, TERMINAL).getBody();
                        log.info("Buchunganfrage for type {} personalnummer:{} - zeitausgleich von: {}, zsp {}", buchungType, dnNr, zeitausgleichVon, zeitspeicherValue);
                    } catch (LHRWebClientException e) {
                        log.error("LHR client returned error for sending zeitausgleichVon-{} for personalnummer-{}: {}", zeitausgleichVon, dnNr, e.getMessage());
                        throw new TechnicalException("LHR-service error", e.getHttpStatus());
                    }
                    buchunganfrageList.remove(zeitausgleichVon);

                    String zeitausgleichBis = zeitausgleichQueue.poll();

                    if (!buchunganfrageList.contains(zeitausgleichBis)) {
                        try {
                            AnfrageSuccessDto anfrageBis = lhrClient.postBuchunganfrage(firma, firmaNr, dnNr,
                                    buchungType, zeitausgleichBis, ORIGINAL_JA, zeitspeicherValue, TERMINAL).getBody();
                            log.info("Buchunganfrage for type {} personalnummer:{} - zeitausgleich bis: {}, zsp {}", buchungType, dnNr, zeitausgleichBis, zeitspeicherValue);
                        } catch (LHRWebClientException e) {
                            log.error("LHR client returned error for sending zeitausgleichBis-{} for personalnummer-{}: {}", zeitausgleichBis, dnNr, e.getMessage());
                            throw new TechnicalException("LHR-service error", e.getHttpStatus());
                        }
                    }
                }
            }
        }

        List<String> zeitbuchungPausen = new ArrayList<>();
        List<String> zeitbuchungPauseBis = new ArrayList<>();

        for (Zeitbuchung zeitbuchung : zeitbuchungs) {
            if (zeitbuchung.getPauseVon() != null && zeitbuchung.getPauseBis() != null) {
                zeitbuchungPausen.add(mergeLocalDateTime(zeitbuchung.getLeistungserfassung().getLeistungsdatum(), zeitbuchung.getPauseVon()));
                zeitbuchungPauseBis.add(mergeLocalDateTime(zeitbuchung.getLeistungserfassung().getLeistungsdatum(), zeitbuchung.getPauseBis()));
            }
        }

        buchunganfrageList.removeAll(zeitbuchungPausen);
        for (String buchung : buchunganfrageList) {
            String zeitspeicherValue = findKeyByValue(buchungsMap, buchung);
            try {
                AnfrageSuccessDto anfrage = lhrClient.postBuchunganfrage(firma,
                                firmaNr,
                                dnNr,
                                buchungType,
                                buchung,
                                ORIGINAL_JA,
                                !ZEITSPEICHER_UNKNOWN.equals(zeitspeicherValue) ? zeitspeicherValue : null,
                                TERMINAL)
                        .getBody();
                log.info("Buchunganfrage for type {} id:{} - buchung:{}, zsp {}", buchungType, anfrage != null ? anfrage.getAnfrage() : "", buchung, zeitspeicherValue);
            } catch (LHRWebClientException e) {
                log.error("LHR client returned error for sending buchung-{} for personalnummer-{}: {}", buchung, dnNr, e.getMessage());
                throw new TechnicalException("LHR-service error", e.getHttpStatus());
            }
        }

        LocalDateTime maxBuchungszeit = buchunganfrageList.stream()
                .map(LocalDateTime::parse)
                .max(Comparator.naturalOrder())
                .orElse(null);

        LocalDateTime maxPauseBisZeit = zeitbuchungPauseBis.stream()
                .map(LocalDateTime::parse)
                .max(Comparator.naturalOrder())
                .orElse(null);

        if (maxPauseBisZeit != null && maxBuchungszeit != null && maxPauseBisZeit.isAfter(maxBuchungszeit)) {
            zeitbuchungPausen.add(maxPauseBisZeit.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        for (String buchung : zeitbuchungPausen) {
            try {
                AnfrageSuccessDto anfrage = lhrClient.postBuchunganfrage(firma, firmaNr, dnNr, buchungType, buchung, ORIGINAL_JA, BUCHUNGANFRAGE_PAUSE, TERMINAL).getBody();
                log.info("Buchunganfrage id:{} - pause-buchung:{}", anfrage != null ? anfrage.getAnfrage() : "", buchung);
            } catch (LHRWebClientException e) {
                log.error("LHR client returned error for sending pause buchung-{} for personalnummer-{}: {}", buchung, dnNr, e.getMessage());
                throw new TechnicalException("LHR-service error", e.getHttpStatus());
            }
        }

        return ResponseEntity.ok().build();
    }


    private void postZeitbuchungAbwesenheit(List<Abwesenheit> abwesenheitList, String grund, String firma, Integer firmaNr) {
        List<Abwesenheit> toBeAdded;
        if (grund.equals(URLAUB_GRUND)) {
            toBeAdded = manageExistingAbwesenheiten(abwesenheitList, firma, firmaNr);
        } else {
            toBeAdded = abwesenheitList;
        }

        for (Abwesenheit abwesenheit : toBeAdded) {
            EintrittDto eintritt = EintrittDto.builder()
                    .grund(grund)
                    .zeitangabe(new ZeitangabeDto(
                            abwesenheit.getVon().format(DateTimeFormatter.ISO_DATE),
                            abwesenheit.getBis().format(DateTimeFormatter.ISO_DATE)
                    ))
                    .source(TERMINAL)
                    .build();

            Integer personalnummer = parseStringToInteger(abwesenheit.getPersonalnummer().getPersonalnummer());
            DienstnehmerRefDto dnRef = DienstnehmerRefDto.builder().faKz(firma).faNr(firmaNr).dnNr(personalnummer).build();
            DnEintritteDto dnEintritte = new DnEintritteDto(dnRef, List.of(eintritt));

            try {
                ResponseEntity<DnEintritteDto> response = lhrClient.postEintritt(firma, firmaNr, personalnummer, null, null, dnEintritte);
                ResponseEntity<DnEintrittDto> getResponse = lhrClient.getEintritt(firma, firmaNr, personalnummer, URLAUB_GRUND, abwesenheit.getVon().toString());
                if (grund.equals(URLAUB_GRUND)) {
                    Integer lhrId = null;
                    if (getResponse != null && getResponse.getBody() != null && getResponse.getBody().getEintritt() != null && getResponse.getBody().getEintritt().getId() != null) {
                        lhrId = getResponse.getBody().getEintritt().getId();
                    }
                    createAbwesenheit(grund, "Urlaub", abwesenheit.getVon(), abwesenheit.getBis(), AbwesenheitStatus.ACCEPTED, abwesenheit.getPersonalnummer(), lhrId);
                }
            } catch (LHRWebClientException e) {
                log.error("Error occured while submitting abwesenheit: {}", e.getMessage());
            }
        }

    }

    private List<Abwesenheit> manageExistingAbwesenheiten(List<Abwesenheit> abwesenheitList, String firma, Integer firmaNr) {
        List<Abwesenheit> toBeAdded = new ArrayList<>();
        for (Abwesenheit abwesenheit : abwesenheitList) {
            Personalnummer personalnummer = abwesenheit.getPersonalnummer();
            List<Abwesenheit> existingAbwesenheiten = abwesenheitService.findAbwesenheitBetweenDates(personalnummer.getId(), abwesenheit.getVon(), abwesenheit.getBis());
            if (!existingAbwesenheiten.isEmpty() && existingAbwesenheiten.size() > 1) {
                existingAbwesenheiten.forEach(abw -> {
                    lhrClient.deleteEintritt(firma, firmaNr, parseStringToInteger(personalnummer.getPersonalnummer()), URLAUB_GRUND, abw.getVon().toString(), null, null);
                    abwesenheitService.deleteById(abw.getId());
                });
                log.info("Multiple Abwesenheiten found for personalnummer : {} and dateVon {} and dateBis {}, deleting them", personalnummer, abwesenheit.getVon().toString(), abwesenheit.getBis().toString());
                toBeAdded.add(abwesenheit);
            } else if (existingAbwesenheiten.size() == 1 && (!existingAbwesenheiten.get(0).getVon().equals(abwesenheit.getVon()) || !existingAbwesenheiten.get(0).getBis().equals(abwesenheit.getBis()))) {
                lhrClient.deleteEintritt(firma, firmaNr, parseStringToInteger(personalnummer.getPersonalnummer()), URLAUB_GRUND, existingAbwesenheiten.get(0).getVon().toString(), null, null);
                log.info("Single Abwesenheit found for personalnummer : {} and dateVon {} and dateBis {}, deleting them", personalnummer, abwesenheit.getVon().toString(), abwesenheit.getBis().toString());
                abwesenheitService.deleteById(existingAbwesenheiten.get(0).getId());
                toBeAdded.add(abwesenheit);
            } else if (existingAbwesenheiten.isEmpty()) {
                toBeAdded.add(abwesenheit);
            }
        }
        return toBeAdded;
    }

    private void createAbwesenheit(String grund, String beschreibung, LocalDate von, LocalDate bis, AbwesenheitStatus status, Personalnummer personalnummer, Integer lhrId) {
        Abwesenheit abwesenheit = new Abwesenheit();
        abwesenheit.setPersonalnummer(personalnummer);
        abwesenheit.setBeschreibung(beschreibung);
        abwesenheit.setGrund(grund);
        if (lhrId != null) {
            abwesenheit.setIdLhr(lhrId);
        }
        Benutzer fuehrungskraft = helperService.getFuehrungskraefte(personalnummer);
        if (fuehrungskraft != null) {
            Set<Benutzer> fuehrungskraefte = new HashSet<>();
            fuehrungskraefte.add(fuehrungskraft);
            abwesenheit.setFuehrungskraefte(fuehrungskraefte);
        }
        abwesenheit.setVon(von);
        abwesenheit.setBis(bis);
        abwesenheit.setStatus(status);
        abwesenheit.setCreatedBy(LHR_SERVICE);
        abwesenheitService.save(abwesenheit);
    }

    private void createZeitausgleich(Personalnummer personalnummer, LocalDate datum, LocalTime von, LocalTime bis, AbwesenheitStatus status) {
        List<Leistungserfassung> leistungserfassungs = leistungserfassungService
                .findByPersonalnummerAndDate(personalnummer, datum.toString());

        List<Zeitausgleich> existing = zeitausgleichService
                .findByPersonalnummerAndDate(personalnummer.getId(), datum);

        if (!leistungserfassungs.isEmpty()) {
            for (Leistungserfassung leistungserfassung : leistungserfassungs) {
                List<Zeitbuchung> zeitbuchungList = zeitbuchungService.getZeitbuchungenByListungserfassen(leistungserfassung);
                if (!zeitbuchungList.isEmpty()) {
                    //This means that there is an entry from the old system and we don't need to create ZA additionally, we just need to delete the entry that overlaps with ibosNG
                    deleteZeitausgelich(personalnummer.getId(), datum.toString());
                    existing.forEach(zeitausgleich -> zeitausgleichService.deleteById(zeitausgleich.getId()));
                    return; // <<< STOP EXECUTION HERE
                }
            }
        }


        if (!existing.isEmpty()) {
            deleteZeitausgelich(personalnummer.getId(), datum.toString());
            existing.forEach(zeitausgleich -> zeitausgleichService.deleteById(zeitausgleich.getId()));
        }

        List<Zeitausgleich> existingZeitausgleich = zeitausgleichService
                .findByPersonalnummerAndDate(personalnummer.getId(), datum);

        if (existingZeitausgleich.size() > 1) {
            existingZeitausgleich.forEach(zg -> zeitausgleichService.deleteById(zg.getId()));
            log.info("Multiple Zeitausgleiche found for personalnummer : {} and date {}, deleting them", personalnummer, datum);
        } else if (existingZeitausgleich.size() == 1 &&
                (!existingZeitausgleich.get(0).getTimeVon().equals(von)
                        || !existingZeitausgleich.get(0).getTimeBis().equals(bis))) {
            zeitausgleichService.deleteById(existingZeitausgleich.get(0).getId());
            log.info("Single Zeitausgleich found for personalnummer : {} and date {}, either von or bis do not match, deleting", personalnummer, datum);
        } else {
            Zeitausgleich zeitausgleich = new Zeitausgleich();
            zeitausgleich.setPersonalnummer(personalnummer);
            zeitausgleich.setDatum(datum);
            zeitausgleich.setTimeVon(von);
            zeitausgleich.setTimeBis(bis);
            zeitausgleich.setStatus(status);
            zeitausgleich.setCreatedBy(LHR_SERVICE);
            zeitausgleichService.save(zeitausgleich);
            log.info("No Zeitausgleich found for personalnummer : {} and date {}, it was created", personalnummer, datum);
        }
    }

    @Override
    public void manageAbwesenheitenVonZeitbuchungen(Integer personalnummerId, String from, String to) {
        Personalnummer personalnummerEntity = personalnummerService.findById(personalnummerId).orElse(null);
        List<Zeitbuchung> zeitbuchungList = zeitbuchungService.findZeitbuchungenInPeriodAndAnAbwesenheit(personalnummerId, parseDate(from), parseDate(to), false);
        Map<Zeitbuchungstyp, List<Zeitbuchung>> mappedZeitbuchungen = zeitbuchungList
                .stream()
                .filter(zeitbuchung -> Boolean.TRUE.equals(zeitbuchung.getZeitbuchungstyp().getIsLhrEintritt()))
                .collect(Collectors.groupingBy(Zeitbuchung::getZeitbuchungstyp));


        for (Map.Entry<Zeitbuchungstyp, List<Zeitbuchung>> entry : mappedZeitbuchungen.entrySet()) {
            List<Abwesenheit> abwesenheitList = new ArrayList<>();
            List<Zeitbuchung> zeitbuchungen = entry.getValue();

            if (!zeitbuchungen.isEmpty()) {
                zeitbuchungen.sort(Comparator.comparing(z -> z.getLeistungserfassung().getLeistungsdatum()));

                LocalDate von = zeitbuchungen.get(0).getLeistungserfassung().getLeistungsdatum();
                LocalDate bis = von;
                for (int i = 1; i < zeitbuchungen.size(); i++) {
                    LocalDate current = zeitbuchungen.get(i).getLeistungserfassung().getLeistungsdatum();
                    // Check if the current date is consecutive to 'bis'
                    if (ChronoUnit.DAYS.between(bis, current) == 1) {
                        bis = current; // Extend the range
                    } else {
                        // Post the previous range
                        Zeitbuchung first = zeitbuchungen.get(i - 1);
                        Abwesenheit abwesenheit = new Abwesenheit();
                        abwesenheit.setVon(von);
                        abwesenheit.setBis(bis);
                        abwesenheit.setPersonalnummer(first.getLeistungserfassung().getPersonalnummer());
                        abwesenheitList.add(abwesenheit);
                        // Start a new range
                        von = current;
                        bis = current;
                    }
                }
                // Post the last detected range
                Zeitbuchung last = zeitbuchungen.get(zeitbuchungen.size() - 1);
                Abwesenheit abwesenheit = new Abwesenheit();
                abwesenheit.setVon(von);
                abwesenheit.setBis(bis);
                abwesenheit.setPersonalnummer(last.getLeistungserfassung().getPersonalnummer());
                abwesenheitList.add(abwesenheit);
            }
            postZeitbuchungAbwesenheit(abwesenheitList,
                    entry.getKey().getLhrKz(),
                    lhrEnvironmentService.getFaKz(personalnummerEntity.getFirma()),
                    lhrEnvironmentService.getFaNr(personalnummerEntity.getFirma()));
        }
    }

    private boolean isBuchungsAnfrage(Zeitbuchung zeitbuchung) {
        return
                //Get all the Anwesenheiten
                (Boolean.TRUE.equals(zeitbuchung.getAnAbwesenheit()) && zeitbuchung.getZeitbuchungstyp() != null &&
                        !BUCHUNGANFRAGE_PAUSE.equals(String.valueOf(zeitbuchung.getZeitbuchungstyp().getLhrZeitspeicher())))
                        ||
                        // ALthough BILDUNGSFREISTELLUNG has a Zeitspeicher, it should be included in the Anwesenheiten
                        (zeitbuchung.getZeitbuchungstyp() != null &&
                                Boolean.FALSE.equals(zeitbuchung.getZeitbuchungstyp().getIsLhrEintritt()) &&
                                BUCHUNGANFRAGE_BILDUNGSFREISTELLUNG.equals(String.valueOf(zeitbuchung.getZeitbuchungstyp().getLhrZeitspeicher())))
                        ||
                        //All the entries in the Zeitbuchungstyp that do not have Zeitspeicher correspond the Anwesenheiten
                        (zeitbuchung.getZeitbuchungstyp() != null &&
                                zeitbuchung.getZeitbuchungstyp().getLhrZeitspeicher() == null);
    }

    private Map<String, List<String>> prepareAndSquashBuchungAnfrage(List<Zeitbuchung> zeitbuchungen) {
        Map<String, List<String>> result = new HashMap<>();

        Map<String, List<Zeitbuchung>> mappedZeitbuchungenWieBuchungen = zeitbuchungen
                .stream()
                .filter(this::isBuchungsAnfrage)
                .collect(Collectors.groupingBy(zeitbuchung ->
                        (zeitbuchung.getZeitbuchungstyp() != null && zeitbuchung.getZeitbuchungstyp().getLhrZeitspeicher() != null) ? String.valueOf(zeitbuchung.getZeitbuchungstyp().getLhrZeitspeicher()) : ZEITSPEICHER_UNKNOWN));


        for (Map.Entry<String, List<Zeitbuchung>> entry : mappedZeitbuchungenWieBuchungen.entrySet()) {
            List<String> databuchungs = new ArrayList<>();
            List<Zeitbuchung> zeitbuchungenEntries = entry.getValue();

            for (Zeitbuchung zeitbuchung : zeitbuchungenEntries) {
                databuchungs.add(mergeLocalDateTime(zeitbuchung.getLeistungserfassung().getLeistungsdatum(), zeitbuchung.getVon()));
                if (zeitbuchung.getPauseBis() != null) {
                    databuchungs.add(mergeLocalDateTime(zeitbuchung.getLeistungserfassung().getLeistungsdatum(), zeitbuchung.getPauseBis()));
                }
                databuchungs.add(mergeLocalDateTime(zeitbuchung.getLeistungserfassung().getLeistungsdatum(), zeitbuchung.getBis()));
            }

            List<String> values = databuchungs.stream()
                    .filter(buchung -> databuchungs.stream().filter(Objects::nonNull).filter(innerBuchung -> innerBuchung.equals(buchung)).toList().size() % 2 != 0)
                    .distinct()
                    .sorted()
                    .toList();
            result.put(entry.getKey(), values);
        }
        return result;

    }

    private List<String> prepareAndSquashZeitausgleich(List<Zeitbuchung> zeitbuchungen) {
        List<String> databuchungs = new ArrayList<>();

        for (Zeitbuchung zeitbuchung : zeitbuchungen) {
            databuchungs.add(mergeLocalDateTime(zeitbuchung.getLeistungserfassung().getLeistungsdatum(), zeitbuchung.getVon()));
            databuchungs.add(mergeLocalDateTime(zeitbuchung.getLeistungserfassung().getLeistungsdatum(), zeitbuchung.getBis()));
        }

        return databuchungs.stream().distinct().sorted().toList();
    }

    @Override
    public ResponseEntity<?> syncUrlaubDetails(Integer personalnummerId, String startDate, String endDate) {
        Personalnummer personalnummerObject = personalnummerService.findById(personalnummerId).orElse(null);
        if (personalnummerObject == null) {
            log.error("Personalnummer object not found for {} in syncUrlaubDetails", personalnummerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String firma = lhrEnvironmentService.getFaKz(personalnummerObject.getFirma());
        Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummerObject.getFirma());

        LocalDate dateVon;
        if (!isNullOrBlank(startDate) && parseDate(startDate) != null) {
            dateVon = parseDate(startDate).withDayOfMonth(1);
        } else {
            dateVon = LocalDate.now().withDayOfMonth(1).withMonth(1);
        }

        LocalDate dateBis;
        if (!isNullOrBlank(endDate) && parseDate(endDate) != null) {
            LocalDate datum = parseDate(endDate);
            dateBis = datum.withDayOfMonth(datum.lengthOfMonth());
        } else {
            dateBis = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        }
        Integer personalnummerInteger = parseStringToInteger(personalnummerObject.getPersonalnummer());

        try {
            ResponseEntity<DnEintritteDto> dnEintritteResponseEntity = lhrClient.getEintritte(firma, firmaNr, personalnummerInteger, null, new String[]{URLAUB_ART});
            DnEintritteDto dnEintritte = dnEintritteResponseEntity.getBody();

            if (dnEintritte == null || dnEintritte.getEintritte() == null) {
                log.error("No urlaubs found for personalnummer {}", personalnummerInteger);
                return ResponseEntity.unprocessableEntity().build();
            }

            List<Abwesenheit> abwesenheiten = dnEintritte.getEintritte().stream()
                    .map(entr -> lhrMapperService.saveIfNotExists(entr, personalnummerObject, AbwesenheitStatus.ACCEPTED))
                    .map(abw -> saveKonsumForAbwesenheit(personalnummerInteger, firma, firmaNr, abw))
                    .toList();


            UrlaubsdatenStandaloneDto urlaubsdatenStandalone = lhrClient.getUrlaube(firma, firmaNr, personalnummerInteger,
                    dateVon.format(DateTimeFormatter.ISO_DATE), dateBis.format(DateTimeFormatter.ISO_DATE)).getBody();

            lhrMapperService.mapUrlaubsdatenAndUpdate(urlaubsdatenStandalone, personalnummerObject);

            return ResponseEntity.ok(abwesenheiten.stream().map(abwesenheitService::mapToAbwesenheitDetailedDto).toList());
        } catch (LHRWebClientException e) {
            log.error("Error getting DnUrlaubsDetails for personalnummer {}: {}", personalnummerInteger, e.getMessage());
            return handleLHRWebClientException(e, String.valueOf(personalnummerInteger), "getAllDnUrlaubsDetails");
        }
    }

    private Abwesenheit saveKonsumForAbwesenheit(Integer personalnummer, String firma, Integer firmaNr, Abwesenheit abwesenheit) {
        double result = 0;
        double verbaucht = 0;
        for (LocalDate currDate = abwesenheit.getVon().withDayOfMonth(1).withMonth(1); !currDate.isAfter(LocalDate.of(abwesenheit.getBis().getYear(), 12, 31)); currDate = currDate.plusMonths(1).withDayOfMonth(1)) {
            UrlaubsdatenStandaloneDetailsDto details = lhrClient
                    .getUrlaubeDetails(firma, firmaNr, personalnummer, currDate.withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE))
                    .getBody();

            if (details == null) {
                continue;
            }

            for (UrlaubswertDailyDto uDaily : details.getUrlaubsdaten()) {
                for (UrlaubswertDailyDaysDto u : uDaily.getDays()) {
                    verbaucht += Math.ceil(u.getAnspruchsverlauf().getKonsum());
                    if (!parseDate(u.getDay()).isAfter(abwesenheit.getBis()) && !parseDate(u.getDay()).isBefore(abwesenheit.getVon())) {
                        result += Math.ceil(u.getAnspruchsverlauf().getKonsum());
                    }
                    if (parseDate(u.getDay()).isEqual(abwesenheit.getBis())) {
                        abwesenheit.setTage(result);
                        abwesenheit.setSaldo(Math.ceil(u.getAnspruchsverlauf().getRestAliquot().getRounded()));
                        if (isNullOrBlank(abwesenheit.getGrund())) {
                            abwesenheit.setTyp(EnumUtils.getEnum(AbwesenheitType.class, abwesenheit.getGrund()).getValue());
                        }
                        abwesenheit.setChangedBy(LHR_SERVICE);
                    }
                }
            }
            abwesenheit.setVerbaucht(verbaucht);
        }

        return abwesenheitService.save(abwesenheit);
    }

    @Override
    public void calculateAbwesenheiten() {
        List<Abwesenheit> abwesenheiten = abwesenheitService.findAllUncalculatedAbwesenheit();
        for (Abwesenheit abwesenheit : abwesenheiten) {
            String firma = lhrEnvironmentService.getFaKz(abwesenheit.getPersonalnummer().getFirma());
            Integer firmaNr = lhrEnvironmentService.getFaNr(abwesenheit.getPersonalnummer().getFirma());

            saveKonsumForAbwesenheit(parseStringToInteger(abwesenheit.getPersonalnummer().getPersonalnummer()), firma, firmaNr, abwesenheit);
        }
    }

    @Override
    public ResponseEntity<?> deleteZeitausgelich(Integer personalnummerId, String date, boolean forceDelete) {
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (pn == null || pn.getFirma() == null) {
            log.error("PersonalnummerId object not found for {} in deleteZeitausgelich", personalnummerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final String firma = lhrEnvironmentService.getFaKz(pn.getFirma());
        final Integer firmaNr = lhrEnvironmentService.getFaNr(pn.getFirma());

        LocalDate localDate = parseDate(date);
        if (localDate == null) {
            log.error("Invalid date format for {}", date);
            return ResponseEntity.unprocessableEntity().build();
        }

        List<Zeitausgleich> existingZeitausgleichs = zeitausgleichService.findByPersonalnummerAndDate(personalnummerId, localDate);
        if (existingZeitausgleichs == null || existingZeitausgleichs.isEmpty()) {
            log.error("Zeitausgleich object not found for personalnummer: {}, date: {}", pn.getPersonalnummer(), date);
            return ResponseEntity.unprocessableEntity().build();
        }
        for (Zeitausgleich zeitausgleich : existingZeitausgleichs) {
            if (List.of(AbwesenheitStatus.INVALID, AbwesenheitStatus.CANCELED, AbwesenheitStatus.ACCEPTED, AbwesenheitStatus.ERROR, AbwesenheitStatus.REJECTED)
                    .contains(zeitausgleich.getStatus())
                    && !forceDelete) {
                log.error("Wrong status {} id={}", zeitausgleich.getStatus(), zeitausgleich.getId());
                continue;
            }

            boolean result = sendZeitausgleichToLhr(pn.getId(), firmaNr, firma, BUCHUNGANFRAGE_D, mergeLocalDateTime(zeitausgleich.getDatum(),
                    zeitausgleich.getTimeVon()), mergeLocalDateTime(zeitausgleich.getDatum(), zeitausgleich.getTimeBis()));

            if (result) {
                log.info("Zeitausgleich {} succesfully deleted from lhr", zeitausgleich);
                zeitausgleich.setStatus(AbwesenheitStatus.CANCELED);
                zeitausgleichService.save(zeitausgleich);

                asyncService.asyncExecutor(() -> syncUrlaubDetails(pn.getId(), zeitausgleich.getDatum().toString(), zeitausgleich.getDatum().toString()));
            } else {
                log.error("Zeitausgleich {} failed to delete from lhr", zeitausgleich);
                return ResponseEntity.unprocessableEntity().build();
            }
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteZeitausgelich(Integer personalnummerId, String date) {
        return deleteZeitausgelich(personalnummerId, date, false);
    }

    @Override
    public boolean sendZeitausgleichToLhr(Integer personalnummerId, Integer firmaNr, String firma, String buchungType,
                                          String dateTimeVon, String dateTimeBis) {
        if (isNullOrBlank(dateTimeVon) || isNullOrBlank(dateTimeBis)) {
            log.error("Invalid date or time format for {}-{}", dateTimeBis, dateTimeVon);
            return false;
        }
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (pn == null || pn.getFirma() == null) {
            log.error("PersonalnummerId object not found for {} in deleteZeitausgelich", personalnummerId);
            return false;
        }
        try {
            ResponseEntity<AnfrageSuccessDto> dnAnfrageVon = lhrClient.postBuchunganfrage(firma, firmaNr, parseStringToInteger(pn.getPersonalnummer()),
                    buchungType, dateTimeVon, ORIGINAL_JA, BUCHUNGANFRAGE_ZEITAUSGLEICH, TERMINAL);
            ResponseEntity<AnfrageSuccessDto> dnAnfrageBis = lhrClient.postBuchunganfrage(firma, firmaNr, parseStringToInteger(pn.getPersonalnummer()),
                    buchungType, dateTimeBis, ORIGINAL_JA, BUCHUNGANFRAGE_ZEITAUSGLEICH, TERMINAL);

            if (dnAnfrageVon.getStatusCode().is2xxSuccessful() && dnAnfrageBis.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully submitted-{} von-{} and bis-{}", buchungType,
                        dnAnfrageVon.getBody() != null ? dnAnfrageVon.getBody().getAnfrage() : "null",
                        dnAnfrageBis.getBody() != null ? dnAnfrageBis.getBody().getAnfrage() : "null");
                return true;
            } else {
                log.error("Failed to submit-{} von-{} and bis-{}", buchungType,
                        dnAnfrageVon.getBody() != null ? dnAnfrageVon.getBody().getAnfrage() : "null",
                        dnAnfrageBis.getBody() != null ? dnAnfrageBis.getBody().getAnfrage() : "null");
                return false;
            }
        } catch (LHRException e) {
            log.error("Failed to post zeitausgleich-{}: {}-{}", buchungType, dateTimeVon, dateTimeBis);
            return false;
        }
    }

    @Override
    public void compareAndUpdateErroneousUrlaube() {
        final LocalDate relevantData = LocalDate.now().withDayOfMonth(1);
        final List<AbwesenheitStatus> relevantStatuses = List.of(AbwesenheitStatus.VALID, AbwesenheitStatus.REQUEST_CANCELLATION);

        List<Personalnummer> personalnummeren = abwesenheitService.findAbwesenheitPersonalnummerFromDateAndStatusesIn(relevantData, relevantStatuses);
        for (Personalnummer personalnummer : personalnummeren) {
            final String firma = lhrEnvironmentService.getFaKz(personalnummer.getFirma());
            final Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummer.getFirma());
            List<Abwesenheit> abwesenheitenIbos = abwesenheitService.findAbwesenheitBetweenDatesAndStatuses(personalnummer.getId(), relevantData, LocalDate.of(2100, 1, 1), relevantStatuses);
            ResponseEntity<DnEintritteDto> response = lhrClient.getEintritte(firma, firmaNr, parseStringToInteger(personalnummer.getPersonalnummer()), null, null);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.debug("Personalnummer-{} urlaube processing skipped, lhr return erroneous response", personalnummer.getPersonalnummer());
                continue;
            }
            List<AbwesenheitDto> abwesenheitenLhr = response.getBody().getEintritte().stream().map(lhrMapperService::mapDnEintrittDto).toList();
            abwesenheitenLhr.forEach(abwesenheitDto -> abwesenheitDto.setPersonalnummerId(personalnummer.getId()));

            for (Abwesenheit abwesenheit : abwesenheitenIbos) {
                AbwesenheitDto abwesenheitDto = abwesenheitService.mapToAbwesenheitDto(abwesenheit);
                if (abwesenheitenLhr.stream().noneMatch(aLhr -> compareAbwesenheitDtos(abwesenheitDto, aLhr))) {
                    log.info("Erronous abwesenheit found, id-{}", abwesenheit.getId());

                    abwesenheit.setStatus(AbwesenheitStatus.ERROR);
                    abwesenheit.setChangedOn(getLocalDateNow());
                    abwesenheit.setChangedBy(LHR_SERVICE);

                    abwesenheitService.save(abwesenheit);

                    //email sending
                    final Benutzer ma = benutzerService.findByPersonalnummer(personalnummer);
                    if (ma == null) {
                        log.warn("No stammdaten found for personalnummer - {}, email sending skipped", personalnummer.getPersonalnummer());
                        continue;
                    }


                    final String nameMitarbeiter = ma.getFirstName() + " " + ma.getLastName();
                    final String fkEmail = abwesenheit.getFuehrungskraefte().stream().map(Benutzer::getEmail).findFirst().orElse(null);

                    String[] emails = Stream.of(ma.getEmail(), fkEmail).filter(Objects::nonNull).toArray(String[]::new);

                    if (emails.length == 0) {
                        log.warn("No emails found for abwesenheit - {}, email sending skipped", abwesenheit.getId());
                        continue;
                    }

                    mailService.sendEmail("lhr-service.abwesenheit.error",
                            "german",
                            null,
                            emails,
                            toObjectArray(abwesenheit.getVon().format(DateTimeFormatter.ofPattern(DATE_PATTERN_ABWESENHEIT_ERROR)), abwesenheit.getBis().format(DateTimeFormatter.ofPattern(DATE_PATTERN_ABWESENHEIT_ERROR))),
                            toObjectArray(nameMitarbeiter, abwesenheit.getVon().format(DateTimeFormatter.ofPattern(DATE_PATTERN_ABWESENHEIT_ERROR)), abwesenheit.getBis().format(DateTimeFormatter.ofPattern(DATE_PATTERN_ABWESENHEIT_ERROR))));
                }
            }
        }
    }

    private boolean compareAbwesenheitDtos(AbwesenheitDto fromLhr, AbwesenheitDto fromIbos) {
        return Objects.equals(fromLhr.getPersonalnummerId(), fromIbos.getPersonalnummerId()) &&
                Objects.equals(fromLhr.getStartDate(), fromIbos.getStartDate()) &&
                Objects.equals(fromLhr.getEndDate(), fromIbos.getEndDate()) &&
                Objects.equals(fromLhr.getType(), fromIbos.getType());
    }
}
