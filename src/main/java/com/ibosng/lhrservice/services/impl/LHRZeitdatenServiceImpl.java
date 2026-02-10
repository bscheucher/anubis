package com.ibosng.lhrservice.services.impl;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng._service.AsyncService;
import com.ibosng.dbservice.dtos.zeiterfassung.umbuchung.UmbuchungDto;
import com.ibosng.dbservice.dtos.zeiterfassung.umbuchung.UmbuchungMetadataDto;
import com.ibosng.dbservice.dtos.zeiterfassung.umbuchung.Zeitspeicher2ValueDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeiterfassung.Auszahlungsantrag;
import com.ibosng.dbservice.entities.zeiterfassung.AuszahlungsantragStatus;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.dbservice.services.zeiterfassung.AuszahlungsantragService;
import com.ibosng.dbservice.services.zeiterfassung.ZeitspeicherService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import com.ibosng.lhrservice.dtos.StatusAnfrage;
import com.ibosng.lhrservice.dtos.zeitdaten.*;
import com.ibosng.lhrservice.enums.Zeitspeicherrefs;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.lhrservice.services.LHRZeitdatenService;
import com.ibosng.lhrservice.utils.Parsers;
import com.ibosng.microsoftgraphservice.services.MailService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.lhrservice.utils.Constants.LHR_SERVICE;
import static com.ibosng.lhrservice.utils.Helpers.handleLHRWebClientException;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Service
@Slf4j
public class LHRZeitdatenServiceImpl implements LHRZeitdatenService {

    private final static List<Zeitspeicherrefs> ZEITSPEICHERREF_TO_SYNC_FROMPERIODSUMMEN = List.of(Zeitspeicherrefs.UEST_25_PFLICHTIG, Zeitspeicherrefs.UEST_50_FREI, Zeitspeicherrefs.UEST_50_PFLICHTIG, Zeitspeicherrefs.UEST_100_FREI, Zeitspeicherrefs.UEST_100_PFLICHTIG);

    private final LHRClient lhrClient;
    private final LHREnvironmentService lhrEnvironmentService;
    private final PersonalnummerService personalnummerService;
    private final StammdatenService stammdatenService;
    private final BenutzerService benutzerService;
    private final MailService mailService;
    private final IbisFirmaService ibisFirmaService;
    private final LeistungserfassungService leistungserfassungService;
    private final AbwesenheitService abwesenheitService;
    private final ZeitspeicherService zeitspeicherService;
    private final AuszahlungsantragService auszahlungsantragService;
    private final AsyncService asyncService;
    private final GlobalUserHolder globalUserHolder;

    @Getter
    @Value("${nextAuthUrl:#{null}}")
    private String baseURL;

    @Getter
    @Value("${vonVerbauchtLocalDate:#{null}}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate vonVerbauchtLocalDate;

    private final String UMBUCHUNG = "/umbuchung";

    public LHRZeitdatenServiceImpl(
            LHRClient lhrClient,
            LHREnvironmentService lhrEnvironmentService,
            PersonalnummerService personalnummerService,
            StammdatenService stammdatenService,
            BenutzerService benutzerService,
            MailService mailService,
            IbisFirmaService ibisFirmaService,
            LeistungserfassungService leistungserfassungService,
            AbwesenheitService abwesenheitService,
            ZeitspeicherService zeitspeicherService,
            AuszahlungsantragService auszahlungsantragService,
            GlobalUserHolder globalUserHolder,
            AsyncService asyncService) {
        this.lhrClient = lhrClient;
        this.lhrEnvironmentService = lhrEnvironmentService;
        this.personalnummerService = personalnummerService;
        this.stammdatenService = stammdatenService;
        this.benutzerService = benutzerService;
        this.mailService = mailService;
        this.ibisFirmaService = ibisFirmaService;
        this.leistungserfassungService = leistungserfassungService;
        this.abwesenheitService = abwesenheitService;
        this.zeitspeicherService = zeitspeicherService;
        this.auszahlungsantragService = auszahlungsantragService;
        this.asyncService = asyncService;
        this.globalUserHolder = globalUserHolder;
    }

    @Override
    public void checkForAuszahlbareStunden() {
        List<IbisFirma> ibisFirmaList = ibisFirmaService.findAllByStatus(Status.ACTIVE);
        for (IbisFirma ibisFirma : ibisFirmaList) {
            DienstnehmerRefDto dnRef = DienstnehmerRefDto.builder()
                    .faKz(lhrEnvironmentService.getFaKz(ibisFirma))
                    .faNr(lhrEnvironmentService.getFaNr(ibisFirma))
                    .build();

            //Get all the zeitdaten from LHR
            ResponseEntity<DnZeitdatenDto[]> responseEntityDnZeiten = lhrClient.getDienstnehmerZeitDatenAllDienstnehmer(dnRef);


            List<DnZeitdatenDto> dnZeitdatenList = Arrays.stream(Objects.requireNonNull(responseEntityDnZeiten.getBody()))
                    .filter(x -> x.getDienstnehmer() != null && x.getDienstnehmer().getDnNr() != null)
                    .filter(x -> x.getZeitspeicherWerte() != null)
                    .filter(x -> x.getZeitspeicherWerte().stream()
                            .anyMatch(zeitspeicher -> zeitspeicher.getValue() > 0))
                    .toList();

            //Filter emails
            String[] emailAddr = dnZeitdatenList.stream()
                    .filter(dnZeitdaten -> dnZeitdaten.getDienstnehmer() != null && dnZeitdaten.getDienstnehmer().getDnNr() != null)
                    .map(dnZeitdaten -> {
                        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(String.valueOf(dnZeitdaten.getDienstnehmer().getDnNr()));
                        if (personalnummer != null) {
                            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer.getPersonalnummer());
                            if (stammdaten != null) {
                                Benutzer benutzer = benutzerService.findAllByFirstNameAndLastName(stammdaten.getVorname(), stammdaten.getNachname());
                                if (benutzer != null) {
                                    return benutzer.getEmail();
                                }
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toArray(String[]::new);

            //Send Mail
            if (Arrays.stream(emailAddr).findAny().isPresent()) {
                mailService.sendEmail(
                        "lhr-service.ma-auszahlbare-ueberstunden",
                        "german",
                        null,
                        emailAddr,
                        toObjectArray(),
                        toObjectArray(getBaseURL() + UMBUCHUNG));
            } else {
                log.info("No email was sent, as no employees requires Umbuchung");
            }
        }
    }

    @Override
    public void closeMonaten(LocalDate monthForSend) {
        log.info("Closing monat {}", monthForSend);

        List<Personalnummer> personalnummerList = personalnummerService.findAll();

        for (Personalnummer personalnummer : personalnummerList) {
            // this line account for a case when user have no leistungserfassung in monthForSend,
            // but we should still 'use' abwesenheiten from period vonVerbauchtLocalDate - monthForSend
            useMonth(personalnummer, monthForSend);

            if ((personalnummer == null) || (personalnummer.getFirma() == null)) {
                continue;
            }
            if (leistungserfassungService.isLeistungserfassungMonthClosed(personalnummer.getId(), personalnummer.getFirma().getBmdClient(), monthForSend)) {
                log.warn("Month {} for personalnummer {} already closed", monthForSend, personalnummer.getPersonalnummer());
                continue;
            }
            if (!leistungserfassungService.isLeistungserfassungMonthClosedMoxis(personalnummer.getId(), personalnummer.getFirma().getBmdClient(), monthForSend)) {
                log.warn("Month {} for personalnummer {} in incorrectMoxisStatus", monthForSend, personalnummer.getPersonalnummer());
                continue;
            }

            List<Leistungserfassung> leistungserfassungList = leistungserfassungService.findByPersonalnummerAndMonth(personalnummer, monthForSend);

            if (leistungserfassungList.isEmpty()) {
                log.warn("No Leistungserfassung for personalnummer {} and month {}. Closing month not possible", personalnummer.getPersonalnummer(), monthForSend);
                continue;
            }

            try {
                log.info("Personalnummer {} start to sync for monat: {}", personalnummer.getPersonalnummer(), monthForSend);
                String firma = lhrEnvironmentService.getFaKz(personalnummer.getFirma());
                Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummer.getFirma());
                Integer dnNr = Parsers.parseStringToInteger(personalnummer.getPersonalnummer());
                lhrClient.postStatusanfrage(firma, firmaNr, dnNr,
                        monthForSend.withDayOfMonth(10).format(DateTimeFormatter.ISO_DATE),
                        StatusAnfrage.FERTIGMELDUNG);

                leistungserfassungList.forEach(l -> {
                    l.setIsLocked(true);
                    l.setChangedOn(getLocalDateNow());
                    l.setChangedBy(LHR_SERVICE);
                });

                leistungserfassungService.saveAll(leistungserfassungList);
                useMonth(personalnummer, monthForSend);

                log.info("Personalnummer {} successfully synced a monat: {}", personalnummer.getPersonalnummer(), monthForSend.withDayOfMonth(10));
            } catch (LHRWebClientException ex) {
                log.error("LHR client returned error for sending leistungerfassung personalnummer-{}: {}", personalnummer.getPersonalnummer(), ex.getMessage());
                handleLHRWebClientException(ex, personalnummer.getPersonalnummer(), "closeMonaten");
            }

        }
        log.info("Finishing close monat {}", monthForSend);
    }

    private void useMonth(Personalnummer personalnummer, LocalDate month) {
        LocalDate start = getVonVerbauchtLocalDate() == null ? month.withDayOfMonth(1) : getVonVerbauchtLocalDate();
        LocalDate end = month.withDayOfMonth(month.lengthOfMonth());
        List<Abwesenheit> abwesenheitToBeUsed = abwesenheitService.findAbwesenheitBetweenDatesAndStatuses(personalnummer.getId(), start, end, List.of(AbwesenheitStatus.ACCEPTED, AbwesenheitStatus.ACCEPTED_FINAL))
                .stream()
                .filter(abwesenheit ->
                        leistungserfassungService.isLeistungserfassungMonthClosed(personalnummer.getId(), personalnummer.getFirma().getBmdClient(), abwesenheit.getVon()) &&
                                leistungserfassungService.isLeistungserfassungMonthClosed(personalnummer.getId(), personalnummer.getFirma().getBmdClient(), abwesenheit.getBis())
                ).toList();

        abwesenheitToBeUsed.forEach(abwesenheit -> {
            abwesenheit.setStatus(AbwesenheitStatus.USED);
            abwesenheit.setChangedBy(LHR_SERVICE);
            abwesenheit.setChangedOn(getLocalDateNow());
        });

        abwesenheitService.saveAll(abwesenheitToBeUsed);
    }

    @Override
    public ResponseEntity<UmbuchungDto> getPeriodensummen(Integer personalnummerId, String month) {
        final LocalDate monthLocalDate = parseDate(month);
        Personalnummer personalnummerEntity = personalnummerService.findById(personalnummerId).orElse(null);

        if (personalnummerEntity == null) {
            log.error("No personalnummer found for string - {}", personalnummerId);
            return ResponseEntity.badRequest().build();
        }

        String firmaKz = lhrEnvironmentService.getFaKz(personalnummerEntity.getFirma());
        Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummerEntity.getFirma());
        Integer dnNr = parseStringToInteger(personalnummerEntity.getPersonalnummer());

        String zeitspeicherrefs = ZEITSPEICHERREF_TO_SYNC_FROMPERIODSUMMEN.stream().map(Zeitspeicherrefs::getRef).collect(Collectors.joining(", "));
        DnZeitdatenPeriodensummenDto periodensummen = lhrClient.getPeriodensummen(firmaKz, firmaNr, dnNr, monthLocalDate.withDayOfMonth(1).format(DateTimeFormatter.ISO_DATE), zeitspeicherrefs).getBody();

        Map<Integer, Zeitspeicher2ValueDto> zeitspeicher2ValueDtoMap = zeitspeicherService
                .findByZspNrIn(ZEITSPEICHERREF_TO_SYNC_FROMPERIODSUMMEN.stream().map(Zeitspeicherrefs::getRef).map(NumberUtils::toInt).toList())
                .stream()
                .map(z -> Zeitspeicher2ValueDto.builder()
                        .zspNummer(z.getZeitspeicherNummer())
                        .zspName(z.getName())
                        .zspComment(z.getComment())
                        .build())
                .collect(Collectors.toMap(Zeitspeicher2ValueDto::getZspNummer, dto -> dto));

        if (periodensummen == null) {
            log.error("Periodensummen is null for pn-{}, month-{} ", personalnummerEntity.getPersonalnummer(), month);
            return ResponseEntity.noContent().build();
        }

        proccessTagZeitspeicherwerte(periodensummen.getPeriodensummen().getPeriodenschluss(), zeitspeicher2ValueDtoMap, Zeitspeicher2ValueDto::setValue);
        proccessTagZeitspeicherwerte(periodensummen.getPeriodensummen().getAbrechnung(), zeitspeicher2ValueDtoMap, Zeitspeicher2ValueDto::setAbrechnung);
        proccessTagZeitspeicherwerte(periodensummen.getPeriodensummen().getUebertrag(), zeitspeicher2ValueDtoMap, Zeitspeicher2ValueDto::setUebertrag);

        UmbuchungDto umbuchungDto = UmbuchungDto
                .builder()
                .zeitspeicher(zeitspeicher2ValueDtoMap.values().stream().toList())
                .metadata(formUmbuchungMetadata(firmaKz, firmaNr, dnNr, personalnummerEntity, month))
                .build();

        return ResponseEntity.ok(umbuchungDto);
    }

    @Override
    public ResponseEntity<Void> postAuszahlungsanfrage(Integer personalnummerId, String month, UmbuchungDto umbuchungDto) {
        Personalnummer personalnummerEntity = personalnummerService.findById(personalnummerId).orElse(null);

        if (personalnummerEntity == null) {
            log.warn("No personalnummer found for string: {}", personalnummerId);
            return ResponseEntity.badRequest().build();
        }

        final String firma = lhrEnvironmentService.getFaKz(personalnummerEntity.getFirma());
        final Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummerEntity.getFirma());
        final int dnNr = parseStringToInteger(personalnummerEntity.getPersonalnummer());
        DienstnehmerRefDto dnRef = DienstnehmerRefDto.builder().faKz(firma).faNr(firmaNr).dnNr(dnNr).build();

        UmbuchungMetadataDto umbuchungMetadataDto = formUmbuchungMetadata(firma, firmaNr, dnNr, personalnummerEntity, month);
        if (!umbuchungMetadataDto.getIsEligible()) {
            log.warn("Personalnummer {} not eligible to book month {}", dnNr, month);
            return ResponseEntity.badRequest().build();
        }

        List<CompletableFuture<ResponseEntity<AnfrageSuccessDto>>> requests = new ArrayList<>();

        for (Zeitspeicher2ValueDto zeitspeicher2ValueDto : umbuchungDto.getZeitspeicher()) {
            ResponseEntity<AnfrageSuccessDto> response = lhrClient.postAuszahlungsanfrage(dnRef, month, zeitspeicher2ValueDto.getZspNummer(), zeitspeicher2ValueDto.getValue());
            AnfrageSuccessDto anfrageSuccessDto = response.getBody();
            Auszahlungsantrag auszahlungsantrag = new Auszahlungsantrag();
            auszahlungsantrag.setPersonalnummer(personalnummerEntity);
            zeitspeicherService.findByZspNr(zeitspeicher2ValueDto.getZspNummer()).ifPresent(auszahlungsantrag::setZeitspeicher);
            auszahlungsantrag.setAnzahlMinuten(Long.valueOf(zeitspeicher2ValueDto.getValue()));
            if (response.getStatusCode().is2xxSuccessful()) {
                auszahlungsantrag.setAnfrageNr(anfrageSuccessDto.getAnfrage());
                requests.add(asyncService.pollAuszahlungsanfrage(() -> lhrClient.getAuszahlungsanfrage(dnRef, anfrageSuccessDto.getAnfrage()), 30));
                auszahlungsantrag.setStatus(AuszahlungsantragStatus.fromValue(anfrageSuccessDto.getStatus()));
            } else {
                auszahlungsantrag.setStatus(AuszahlungsantragStatus.ERROR);
            }
            auszahlungsantrag.setCreatedOn(getLocalDateNow());
            auszahlungsantrag.setCreatedBy(globalUserHolder.getUsername());
            auszahlungsantragService.save(auszahlungsantrag);
        }

        CompletableFuture.allOf(requests.toArray(new CompletableFuture[0])).join();
        return ResponseEntity.ok().build();
    }

    private void proccessTagZeitspeicherwerte(
            List<TagZeitspeicherwerteDto> tagZeitspeicherwerteList,
            Map<Integer, Zeitspeicher2ValueDto> zeitspeicher2ValueDtoMap,
            BiConsumer<Zeitspeicher2ValueDto, Integer> valueSetter) {
        TagZeitspeicherwerteDto tagZeitspeicherwerte = tagZeitspeicherwerteList.stream().max(Comparator.comparing(tz -> parseDate(tz.getTag()))).orElse(null);
        if (tagZeitspeicherwerte == null) {
            log.warn("tagZeitspeicherwerte is empty, skipping procesing");
            return;
        }

        for (ZeitspeicherwertDto zeitspeicherwert : tagZeitspeicherwerte.getZeitspeicherWerte()) {
            valueSetter.accept(
                    zeitspeicher2ValueDtoMap.get(zeitspeicherwert.getZeitspeicherNummer()),
                    zeitspeicherwert.getValue()
            );
        }
    }

    @Override
    public UmbuchungMetadataDto formUmbuchungMetadata(String firmaKz, Integer firmaNr, Integer dnNr, Personalnummer personalnummer, String month) {
        final LocalDate monthLocalDate = parseDate(month);

        boolean isMonthLhrClosed = leistungserfassungService.isLeistungserfassungMonthClosed(personalnummer.getId(), personalnummer.getFirma().getBmdClient(), monthLocalDate);

        DnZeitdatenPeriodensummenDto periodensummen = lhrClient.getPeriodensummen(firmaKz, firmaNr, dnNr, monthLocalDate.withDayOfMonth(1).toString(), ZEITSPEICHERREF_TO_SYNC_FROMPERIODSUMMEN.stream().map(Zeitspeicherrefs::getRef).collect(Collectors.joining(", "))).getBody();
        boolean umbuchungNotDone = (periodensummen != null) &&
                (periodensummen.getPeriodensummen() != null) &&
                (periodensummen.getPeriodensummen().getAbrechnung() != null) &&
                periodensummen.getPeriodensummen().getAbrechnung().stream()
                        .max(Comparator.comparing((TagZeitspeicherwerteDto tz) -> parseDate(tz.getTag())))
                        .map(abrechnung -> abrechnung.getZeitspeicherWerte().stream().noneMatch(z -> z.getValue() != 0))
                        .orElse(true);

        ZeitdatenStatusDto zeitdatenStatus = lhrClient.geZeitdatenStatus(firmaKz, firmaNr, dnNr).getBody();
        boolean isMonthNotCalculated = (zeitdatenStatus != null) &&
                (zeitdatenStatus.getStatus() != null) &&
                (zeitdatenStatus.getStatus().getAbrechnung() != null) &&
                parseDate(zeitdatenStatus.getStatus().getAbrechnung()).isBefore(monthLocalDate.withDayOfMonth(monthLocalDate.lengthOfMonth()));

        log.info("isEligible calculation for pn-{} and month-[{}]: is month closed in lhr - {}, umbuchung not done - {}, zeitdaten status in Lhr correct - {}", dnNr, month, isMonthLhrClosed, umbuchungNotDone, isMonthNotCalculated);

        UmbuchungMetadataDto umbuchungMetadataDto = UmbuchungMetadataDto.builder()
                .isEligible(!isMonthLhrClosed && umbuchungNotDone && isMonthNotCalculated)
                .build();

        if (periodensummen != null && periodensummen.getPeriodensummen() != null && periodensummen.getPeriodensummen().getAbrechnung() != null) {
            periodensummen.getPeriodensummen().getAbrechnung()
                    .stream()
                    .max(Comparator.comparing((TagZeitspeicherwerteDto tz) -> parseDate(tz.getTag())))
                    .ifPresent(tz -> umbuchungMetadataDto.setDate(tz.getTag()));
        }

        if (isMonthLhrClosed) {
            umbuchungMetadataDto.setReason("Month closed in LHR");
        }

        if (!umbuchungNotDone) {
            umbuchungMetadataDto.setReason("Umbuchung already done for that month");
        }

        if (!isMonthNotCalculated) {
            umbuchungMetadataDto.setReason("Month already calculated in LHR, no umbuchung can be done");
        }

        return umbuchungMetadataDto;
    }
}
