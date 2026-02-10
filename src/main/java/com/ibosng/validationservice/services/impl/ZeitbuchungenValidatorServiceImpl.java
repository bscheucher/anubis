package com.ibosng.validationservice.services.impl;

import com.ibosng._service.AsyncService;
import com.ibosng.dbibosservice.entities.pzleistung.Pzleistung;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbibosservice.services.PzleistungService;
import com.ibosng.dbmapperservice.services.ZeitbuchungenMapperService;
import com.ibosng.dbservice.dtos.ZeitbuchungSyncRequestDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.validationservice.services.Validation2LHRService;
import com.ibosng.validationservice.services.ValidatorService;
import com.ibosng.validationservice.services.ZeitbuchungenValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.dbservice.utils.Parsers.parseDateTime;

@Service
@Slf4j
public class ZeitbuchungenValidatorServiceImpl implements ZeitbuchungenValidatorService {
    private final PzleistungService pzleistungService;
    private final ZeitbuchungenMapperService zeitbuchungenMapper;
    private final ValidatorService validatorService;
    private final AdresseIbosService adresseIbosService;
    private final LeistungserfassungService leistungserfassungService;
    private final ZeitbuchungService zeitbuchungService;
    private final Validation2LHRService validation2LHRService;
    private final PersonalnummerService personalnummerService;
    private final AsyncService asyncService;

    public ZeitbuchungenValidatorServiceImpl(PzleistungService pzleistungService,
                                             ZeitbuchungenMapperService zeitbuchungenMapper,
                                             ValidatorService validatorService,
                                             AdresseIbosService adresseIbosService,
                                             LeistungserfassungService leistungserfassungService,
                                             ZeitbuchungService zeitbuchungService,
                                             Validation2LHRService validation2LHRService,
                                             PersonalnummerService personalnummerService,
                                             AsyncService asyncService) {
        this.pzleistungService = pzleistungService;
        this.zeitbuchungenMapper = zeitbuchungenMapper;
        this.validatorService = validatorService;
        this.adresseIbosService = adresseIbosService;
        this.leistungserfassungService = leistungserfassungService;
        this.zeitbuchungService = zeitbuchungService;
        this.validation2LHRService = validation2LHRService;
        this.personalnummerService = personalnummerService;
        this.asyncService = asyncService;
    }

    @Override
    public List<ZeitbuchungenDto> syncZeitbuchungenForMitarbeiter(Personalnummer personalnummer, Integer adresseIbosId, LocalDate startDate, LocalDate endDate) {

        List<Pzleistung> zeitbuchungList = pzleistungService.findByADadnrInPeriod(adresseIbosId, startDate, endDate);
        Map<String, List<ZeitbuchungenDto>> zeitbuchungenMap = zeitbuchungList
                .stream()
                .map(pzl -> zeitbuchungenMapper.mapToDto(pzl, personalnummer))
                .collect(Collectors.groupingBy(ZeitbuchungenDto::getLeistungsdatum));

        List<CompletableFuture<List<ZeitbuchungenDto>>> shouldBeWaitedFor = new ArrayList<>();
        List<ZeitbuchungenDto> result = new ArrayList<>();
        for (String datum : zeitbuchungenMap.keySet()) { // block1: wait for me
            shouldBeWaitedFor.add(asyncService.processLeistungsdatum(personalnummer, datum, zeitbuchungenMap.get(datum)));
        }

        CompletableFuture.allOf(shouldBeWaitedFor.toArray(new CompletableFuture[0])).join();

        for (CompletableFuture<List<ZeitbuchungenDto>> future : shouldBeWaitedFor) {
            try {
                result.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Async execution exception - {}", e.getMessage());
            }
        }

        asyncService.asyncExecutor(() -> {
            asyncService.zeitbuchungOverlapCheck(personalnummer, startDate, endDate).join();
            validation2LHRService.manageAbsencesFromZeitbuchungen(personalnummer.getId(), startDate.toString(), endDate.toString());
            return true;
        });

        return result;
    }


    @Override
    public ResponseEntity<List<ZeitbuchungenDto>> getZeitbuchungen(ZeitbuchungSyncRequestDto requestDto) {
        final LocalDate startDateLocalDate = parseDate(requestDto.getStartDate());
        final LocalDate endDateLocalDate = parseDate(requestDto.getEndDate());
        Personalnummer personalnummer = personalnummerService.findById(requestDto.getPersonalnummerId()).orElse(null);
        if (personalnummer == null) {
            return ResponseEntity.notFound().build();
        }

        Integer adresseId = adresseIbosService.getAdresseIdFromPersonalnummer(personalnummer.getPersonalnummer(), personalnummer.getFirma().getLhrNr());
        if (adresseId != null) {
            return ResponseEntity.ok(syncZeitbuchungenForMitarbeiter(personalnummer, adresseId, startDateLocalDate, endDateLocalDate));
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ZeitbuchungenDto>> updateZeitbuchungen(ZeitbuchungSyncRequestDto requestDto, String changedBy) {
        final LocalDateTime vonDatum = parseDateTime(requestDto.getStartDate());
        final LocalDateTime bisDatum = parseDateTime(requestDto.getEndDate());

        Personalnummer personalnummer = personalnummerService.findById(requestDto.getPersonalnummerId()).orElse(null);
        if (personalnummer == null) {
            return ResponseEntity.notFound().build();
        }

        Integer parsedAdadnr = adresseIbosService.getAdresseIdFromPersonalnummer(personalnummer.getPersonalnummer(), personalnummer.getFirma().getLhrNr());
        if (parsedAdadnr == null) {
            return ResponseEntity.notFound().build();
        }
        List<ZeitbuchungenDto> zeitbuchungenDtos = new ArrayList<>();

        // Step 1: Fetch all changed Pzleistung records
        List<Pzleistung> pzleistungList = pzleistungService.findByADadnrInChangePeriod(parsedAdadnr, vonDatum, bisDatum);
        List<ZeitbuchungenDto> zeitbuchungenToValidate = pzleistungList.stream()
                .map(pzl -> {
                    log.info("Mapping Pzleistung to DTO: {}", pzl);
                    return zeitbuchungenMapper.mapToDto(pzl, personalnummer);
                })
                .toList();

        // Step 2: Delete outdated Zeitbuchungen for each ZeitbuchungenDto
        for (ZeitbuchungenDto zeitbuchungenDto : zeitbuchungenToValidate) {
            log.info("Fetching Leistungserfassung from related Personalnummer: {} ", zeitbuchungenDto.getPersonalnummer());
            List<Leistungserfassung> leistungserfassungen = leistungserfassungService.findByPersonalnummerAndDate(
                    personalnummer,
                    zeitbuchungenDto.getLeistungsdatum()
            );

            if (leistungserfassungen == null || leistungserfassungen.isEmpty()) {
                log.warn("Leistungserfassungen is unexpectedly empty for personalnummer: {} and date: {}",
                        zeitbuchungenDto.getPersonalnummer(), zeitbuchungenDto.getLeistungsdatum());
                continue;  // Skip further processing for this entry
            }

            for (Leistungserfassung leistungserfassung : leistungserfassungen) {
                // Fetch related Zeitbuchungen for the Leistungserfassung
                List<Zeitbuchung> zeitbuchungenToDelete = zeitbuchungService.getZeitbuchungenByListungserfassen(leistungserfassung);

                // Log or process fetched Zeitbuchungen if needed
                // Delete each fetched Zeitbuchung
                if (Boolean.TRUE.equals(leistungserfassung.getIsSyncedWithLhr())) {
                    validation2LHRService.deleteLeistungserfassung(personalnummer.getId(), leistungserfassung.getLeistungsdatum()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }

                zeitbuchungenToDelete.forEach(zeitbuchung -> {
                            log.info("Deleting Zeitbuchung: {}", zeitbuchung);
                            zeitbuchungService.deleteById(zeitbuchung.getId());
                        }
                );
            }
            zeitbuchungenDtos.addAll(syncZeitbuchungenForMitarbeiter(personalnummer, parsedAdadnr, vonDatum.toLocalDate(), bisDatum.toLocalDate()));
        }
        log.info("Syncing created/updated Zeitbuchungen process has concluded successfully.");
        return ResponseEntity.ok(zeitbuchungenDtos);
    }
}
