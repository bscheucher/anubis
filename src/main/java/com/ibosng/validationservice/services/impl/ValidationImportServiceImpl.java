package com.ibosng.validationservice.services.impl;

import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragFixIbosService;
import com.ibosng.dbmapperservice.services.SeminarProjektMapperService;
import com.ibosng.dbservice.dtos.ZeitausgleichDto;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.*;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.services.*;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.validationservice.services.MitarbeiterSyncService;
import com.ibosng.validationservice.services.Validation2LHRService;
import com.ibosng.validationservice.services.ValidationImportService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.validationservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationImportServiceImpl implements ValidationImportService {

    private final static String URLAUB = "Urlaub";
    private final static String ZEITAUSGLEICH = "Zeitausgleich";
    private final static String FUEHRUNGSKRAFT = "FUEHRUNGSKRAFT";
    private final static String STARTCOACH = "STARTCOACH";

    @Getter
    @Value("${importUEBASeminarsReferenceData:#{null}}")
    private String importUEBASeminarsReferenceData;

    @Getter
    @Value("${importFutureAbwesenheitenReferenceData:#{null}}")
    private String importFutureAbwesenheitenReferenceData;

    @Getter
    @Value("${futureUrlaubeStichtag:#{null}}")
    private String futureUrlaubeStichtag;

    private final IbosReferenceService ibosReferenceService;
    private final SeminarIbosService seminarIbosService;
    private final SeminarProjektMapperService seminarProjektMapperService;
    private final SeminarService seminarService;
    private final DataReferenceTempService dataReferenceTempService;
    private final BenutzerService benutzerService;
    private final MitarbeiterSyncService mitarbeiterSyncService;
    private final AbwesenheitService abwesenheitService;
    private final ArbeitsvertragFixIbosService arbeitsvertragFixIbosService;
    private final Validation2LHRService validation2LHRService;
    private final ZeitausgleichService zeitausgleichService;
    private final VertragsdatenService vertragsdatenService;
    private final AdresseIbosService adresseIbosService;
    private final PersonalnummerService personalnummerService;
    private final StammdatenService stammdatenService;

    @Override
    public void importUEBASeminars() {
        List<IbosReference> seminars = ibosReferenceService.findAllByData(getImportUEBASeminarsReferenceData());
        for (IbosReference seminarReference : seminars) {
            if (seminarReference.getIbosId() != null) {
                List<Seminar> existingSeminars = seminarService.findAllBySeminarNummer(seminarReference.getIbosId());
                if (!existingSeminars.isEmpty()) {
                    log.warn("There are already a seminars found for this seminar number:");
                    existingSeminars.forEach(seminar -> log.warn("Bezeichnung: {}", seminar.getBezeichnung()));
                    continue;
                }
                log.info("Importing UEBASeminar with ID {}", seminarReference.getIbosId());
                Optional<SeminarIbos> seminarIbos = seminarIbosService.findById(seminarReference.getIbosId());
                if (seminarIbos.isPresent()) {
                    Seminar seminar = seminarProjektMapperService.mapSeminarIbosToSeminar(seminarIbos.get());
                    seminar = seminarService.save(seminar);
                    log.info("Successfully imported UEBASeminar with ID in ibos {}, ID in ibosNG {} and Bezeichnung {}", seminarReference.getIbosId(), seminar.getId(), seminar.getBezeichnung());
                }
            }
        }
    }

    @Override
    public void importFutureAbwesenheiten() {
        /**
         * data1 is bmdCLient
         * data2 is PN
         * data3 is Type of Abwesenheit
         * data4 is datumVon
         * data5 is DatumBis
         * data6 is a flag, if it was executed or not
         */
        manageFutureUrlaube();
        manageFutureZeitausgleich();
    }

    private void manageFutureUrlaube() {
        Map<String, String> filters = new HashMap<>();
        filters.put(DataReferenceTempService.REFERENCE, getImportFutureAbwesenheitenReferenceData());
        filters.put(DataReferenceTempService.DATA3, URLAUB);
        filters.put(DataReferenceTempService.DATA6, "false");
        List<DataReferenceTemp> urlaube = dataReferenceTempService.search(filters);
        List<Abwesenheit> savedAbwesenheiten = new ArrayList<>();
        //First save
        for (DataReferenceTemp urlaubeTemp : urlaube) {
            try {
                Benutzer benutzer = getBenutzer(urlaubeTemp);
                if (benutzer == null) {
                    log.warn("MA could not be synced with PN {} and bmdClient {}", urlaubeTemp.getData2(), urlaubeTemp.getData1());
                    continue;
                }
                Abwesenheit abwesenheit = saveUrlaub(urlaubeTemp, benutzer);
                if (abwesenheit != null) {
                    savedAbwesenheiten.add(abwesenheit);
                }
            } catch (Exception e) {
                log.error("An error occurred while importing Urlaub for personalnummer {} with exception", urlaubeTemp.getData2(), e);
            }
        }
        sendUrlaubeToLHRAndUpdateStatuses(savedAbwesenheiten);
        urlaube.forEach(urlaub -> {
            urlaub.setData6("true");
            urlaub.setChangedBy(VALIDATION_SERVICE);
            urlaub.setChangedOn(getLocalDateNow());
        });
        dataReferenceTempService.saveAll(urlaube);
    }

    private void sendUrlaubeToLHRAndUpdateStatuses(List<Abwesenheit> savedAbwesenheiten) {
        for (Abwesenheit abwesenheit : savedAbwesenheiten) {
            AbwesenheitDto abwesenheitDto = abwesenheitService.mapToAbwesenheitDto(abwesenheit);
            try {
                validation2LHRService.sendUrlaub(null, null, abwesenheitDto);
                Optional<Abwesenheit> savedAbwesenheit = abwesenheitService.findById(abwesenheit.getId());
                if (savedAbwesenheit.isPresent() && AbwesenheitStatus.VALID.equals(savedAbwesenheit.get().getStatus())) {
                    abwesenheit.setStatus(AbwesenheitStatus.ACCEPTED);
                    abwesenheitService.save(abwesenheit);
                }
            } catch (Exception e) {
                AbwesenheitDto errornousDto = abwesenheitService.findByIdAndForceRefresh(abwesenheitDto.getId())
                        .map(abwesenheitService::mapToAbwesenheitDto).orElse(null);
                log.error("Error sending Urlaub request to LHR: {}; Dto - {}", e.getMessage(), errornousDto);
            }
        }
    }

    private void manageFutureZeitausgleich() {
        Map<String, String> filters = new HashMap<>();
        filters.put(DataReferenceTempService.REFERENCE, getImportFutureAbwesenheitenReferenceData());
        filters.put(DataReferenceTempService.DATA3, ZEITAUSGLEICH);
        filters.put(DataReferenceTempService.DATA6, "false");
        List<DataReferenceTemp> zeitausgleicheList = dataReferenceTempService.search(filters);
        for (DataReferenceTemp zeitausgleichTemp : zeitausgleicheList) {
            try {
                Benutzer benutzer = getBenutzer(zeitausgleichTemp);
                if (benutzer == null) {
                    log.warn("MA could not be synced with PN {} and bmdClient {}", zeitausgleichTemp.getData2(), zeitausgleichTemp.getData1());
                    continue;
                }
                AbwesenheitDto abwesenheitDto = new AbwesenheitDto();
                LocalDate von = null;
                LocalDate bis = null;
                if (isValidDate(zeitausgleichTemp.getData4())) {
                    von = parseDate(zeitausgleichTemp.getData4());
                    LocalDate stichTag = parseDate(getFutureUrlaubeStichtag());
                    if (von.isBefore(stichTag)) {
                        continue;
                    }
                    abwesenheitDto.setStartDate(von);
                }
                if (isValidDate(zeitausgleichTemp.getData5())) {
                    bis = parseDate(zeitausgleichTemp.getData5());
                    abwesenheitDto.setEndDate(bis);
                }
                if (von != null && bis != null) {
                    abwesenheitDto.setDurationInDays(String.valueOf(ChronoUnit.DAYS.between(von, bis.plusDays(1))));
                }
                abwesenheitDto.setStatus(AbwesenheitStatus.NEW);
                abwesenheitDto.setPersonalnummerId(benutzer.getPersonalnummer().getId());
                abwesenheitDto.setComment("Import");
                List<ZeitausgleichDto> postZeitausgleich = sendZeitausgleicheToLHR(abwesenheitDto);

                List<Zeitausgleich> zeitausgleiche = zeitausgleichService.findAllByIdIn(postZeitausgleich.stream().map(ZeitausgleichDto::getId).collect(Collectors.toList()));
                zeitausgleiche.forEach(zeitausgleich -> {
                    Benutzer fuehrungskraefte = getFuehrungskraefte(benutzer);
                    if (fuehrungskraefte != null) {
                        Set<Benutzer> fuehrungskraefteSet = new HashSet<>();
                        fuehrungskraefteSet.add(fuehrungskraefte);
                        zeitausgleich.setFuehrungskraefte(fuehrungskraefteSet);
                        zeitausgleichService.save(zeitausgleich);
                    }
                    if (AbwesenheitStatus.VALID.equals(zeitausgleich.getStatus())) {
                        zeitausgleich.setStatus(AbwesenheitStatus.ACCEPTED);
                        zeitausgleichTemp.setData6("true");
                        zeitausgleichTemp.setChangedBy(VALIDATION_SERVICE);
                        zeitausgleichTemp.setChangedOn(getLocalDateNow());
                        dataReferenceTempService.save(zeitausgleichTemp);
                        zeitausgleichService.save(zeitausgleich);
                    }
                });
            } catch (Exception e) {
                log.error("An error occurred while importing Zeitausgleich for personalnummer {} with exception", zeitausgleichTemp.getData2(), e);
            }
        }
    }

    private List<ZeitausgleichDto> sendZeitausgleicheToLHR(AbwesenheitDto abwesenheitDto) {
        try {
            List<ZeitausgleichDto> postZeitausgleich = validation2LHRService.sendZeitausgleichPeriod(abwesenheitDto).getBody();
            if (postZeitausgleich != null && !postZeitausgleich.isEmpty()) {
                return postZeitausgleich;
            }
        } catch (Exception e) {
            log.error("Error sending Zeitausgleich request to LHR: {}}", e.getMessage());
        }
        return new ArrayList<>();
    }


    private Benutzer getBenutzer(DataReferenceTemp dataReferenceTemp) {
        Benutzer benutzer = benutzerService.findByPersonalnummerAndFirmaBmdClient(dataReferenceTemp.getData2(), parseStringToInteger(dataReferenceTemp.getData1()));
        if (benutzer == null) {
            String personalnummerString = mitarbeiterSyncService.syncMitarbeiterFromIbisacam("", dataReferenceTemp.getData2(), parseStringToInteger(dataReferenceTemp.getData1())).getBody();
            benutzer = benutzerService.findByPersonalnummerAndFirmaBmdClient(dataReferenceTemp.getData2(), parseStringToInteger(dataReferenceTemp.getData1()));
        }
        return benutzer;
    }

    private Abwesenheit saveUrlaub(DataReferenceTemp urlaubeTemp, Benutzer benutzer) {
        Abwesenheit abwesenheit = new Abwesenheit();
        abwesenheit.setGrund("URLAU");
        abwesenheit.setTyp("URLAU");
        abwesenheit.setBeschreibung(urlaubeTemp.getData3());
        abwesenheit.setPersonalnummer(benutzer.getPersonalnummer());
        abwesenheit.setCreatedBy(VALIDATION_SERVICE);
        LocalDate von = null;
        LocalDate bis = null;
        if (isValidDate(urlaubeTemp.getData4())) {
            von = parseDate(urlaubeTemp.getData4());
            LocalDate stichTag = parseDate(getFutureUrlaubeStichtag());
            if (von.isBefore(stichTag)) {
                return null;
            }
            abwesenheit.setVon(von);
        }
        if (isValidDate(urlaubeTemp.getData5())) {
            bis = parseDate(urlaubeTemp.getData5());
            abwesenheit.setBis(bis);
        }
        if (von != null && bis != null) {
            abwesenheit.setTage((double) ChronoUnit.DAYS.between(von, bis.plusDays(1)));
        }
        abwesenheit.setKommentar("Import");
        abwesenheit.setStatus(AbwesenheitStatus.NEW);

        Benutzer fuehrungskraefte = getFuehrungskraefte(benutzer);
        if (fuehrungskraefte != null) {
            Set<Benutzer> fuehrungskraefteSet = new HashSet<>();
            fuehrungskraefteSet.add(fuehrungskraefte);
            abwesenheit.setFuehrungskraefte(fuehrungskraefteSet);
        }
        return abwesenheitService.save(abwesenheit);
    }

    private Benutzer getFuehrungskraefte(Benutzer benutzer) {
        String fuhrungskraefteUpn = adresseIbosService.getFuehrungskraftUPNFromLogin(benutzer.getUpn().split("@")[0]);
        Benutzer fuehrungskraft = null;
        if (!isNullOrBlank(fuhrungskraefteUpn)) {
            fuhrungskraefteUpn = fuhrungskraefteUpn.toLowerCase();
            fuehrungskraft = benutzerService.findByUpn(fuhrungskraefteUpn);

            if (fuehrungskraft == null) {
                // Sync the supervisor if not found
                mitarbeiterSyncService.syncMitarbeiterFromIbisacamWithUPN(fuhrungskraefteUpn, null, null);

                fuehrungskraft = benutzerService.findByUpn(fuhrungskraefteUpn); // Retry after sync
            }

        }
        return fuehrungskraft;
    }

    @Override
    public void replaceIbosRefenceWithBenutzer() {
        importFuehrungskraefteAndStartcoaches(adresseIbosService.findEmailsOfActiveFuehrungskraefte(), FUEHRUNGSKRAFT);
        importFuehrungskraefteAndStartcoaches(adresseIbosService.findEmailsOfActiveStartcoaches(), STARTCOACH);
        replaceFuehrungskraftReferences();
        replaceStartcoachReferences();
    }

    @Override
    public void importDataFromIbos() {
        List<Personalnummer> personalnummers = personalnummerService.findAllByMitarbeiterTypeAndIsIbosngOnboarded(MitarbeiterType.MITARBEITER, false);
        for (Personalnummer personalnummer : personalnummers) {
            log.info("Starting to update MA data for personalnummer {}", personalnummer.getPersonalnummer());
            IbisFirma firma = personalnummer.getFirma();
            if (firma.getBmdClient() != null) {
                log.info("Trying to get benutzer from personalnummer {} and bmdClient {}", personalnummer.getPersonalnummer(), firma.getBmdClient());
                Benutzer benutzer = benutzerService.findByPersonalnummerAndFirmaBmdClient(personalnummer.getPersonalnummer(), personalnummer.getFirma().getBmdClient());
                if (!isNullOrBlank(benutzer.getEmail())) {
                    log.info("Trying to get sync MA data, calling the sync method with email {}, personalnummer {} and firma LhrNr {}", benutzer.getEmail(), personalnummer.getPersonalnummer(), personalnummer.getFirma().getLhrNr());
                    mitarbeiterSyncService.syncMitarbeiterFromIbisacam(benutzer.getEmail(), personalnummer.getPersonalnummer(), personalnummer.getFirma().getLhrNr());
                }
            } else if (firma.getLhrNr() != null) {
                log.info("Trying to get sync MA data, calling the sync method with personalnummer {} and firma LhrNr {}", personalnummer.getPersonalnummer(), personalnummer.getFirma().getLhrNr());
                mitarbeiterSyncService.syncMitarbeiterFromIbisacam(null, personalnummer.getPersonalnummer(), personalnummer.getFirma().getLhrNr());
            }
        }
    }

    private void importFuehrungskraefteAndStartcoaches(List<String> toBeImported, String reference) {
        for (String email : toBeImported) {
            email = email.toLowerCase();
            Benutzer benutzer = benutzerService.findByEmail(email);
            if (benutzer == null) {
                try {
                    ResponseEntity<String> response = mitarbeiterSyncService.syncMitarbeiterFromIbisacam(email, null, null);
                    if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                        log.warn("Error while syncing mitarbeiter {}: {}", email, response.getBody());
                    } else if (!response.getStatusCode().is2xxSuccessful()) {
                        log.error("Error while syncing mitarbeiter {} with status {}: {}", email, response.getStatusCode(), response.getBody());
                    }
                } catch (Exception e) {
                    log.error("Error while syncing mitarbeiter {}", email, e);
                }
                benutzer = benutzerService.findByEmail(email); // Retry after sync
                if (benutzer != null) {
                    log.info("Successfully added {} with email {}", reference, benutzer.getEmail());
                    createIbosngReference(benutzer.getId(), reference);
                }
            } else {
                List<IbosReference> existing = ibosReferenceService.findAllByIbosngIdAndData(benutzer.getId(), reference);
                if (existing == null || existing.isEmpty()) {
                    createIbosngReference(benutzer.getId(), reference);
                }
            }
        }
    }

    private void createIbosngReference(Integer ibosngId, String data) {
        IbosReference ibosReference = new IbosReference();
        ibosReference.setIbosngId(ibosngId);
        ibosReference.setData(data);
        ibosReference.setStatus(Status.ACTIVE);
        ibosReference.setCreatedBy(VALIDATION_SERVICE);
        ibosReferenceService.save(ibosReference);
    }

    private void replaceFuehrungskraftReferences() {
        List<Vertragsdaten> vertragsdatens = vertragsdatenService.findAllByFuehrungskraftIsNullAndFuehrungskraftRefIsNotNull();
        for (Vertragsdaten vertragsdaten : vertragsdatens) {
            String email = vertragsdaten.getFuehrungskraftRef().getData();
            Benutzer fuehrungskraft = benutzerService.findByEmail(email);
            if (fuehrungskraft == null) {
                // Sync the supervisor if not found
                mitarbeiterSyncService.syncMitarbeiterFromIbisacam(email, null, null);
                fuehrungskraft = benutzerService.findByEmail(email); // Retry after sync
            }
            if (fuehrungskraft != null) {
                vertragsdaten.setFuehrungskraft(fuehrungskraft);
                vertragsdaten.setFuehrungskraftRef(null);
                vertragsdaten.setChangedBy(VALIDATION_SERVICE);
                vertragsdaten.setChangedOn(getLocalDateNow());
                vertragsdatenService.save(vertragsdaten);
                log.info("Successfully replaced fuehrungskraft with email {}", fuehrungskraft.getEmail());
            }
        }
    }

    private void replaceStartcoachReferences() {
        List<Vertragsdaten> vertragsdatens = vertragsdatenService.findAllByStartcoachIsNullAndStartcoachRefIsNotNull();
        for (Vertragsdaten vertragsdaten : vertragsdatens) {
            String email = vertragsdaten.getStartcoachRef().getData();
            Benutzer startcoach = benutzerService.findByEmail(email);
            if (startcoach == null) {
                // Sync the supervisor if not found
                mitarbeiterSyncService.syncMitarbeiterFromIbisacam(email, null, null);
                startcoach = benutzerService.findByEmail(email); // Retry after sync
            }
            if (startcoach != null) {
                vertragsdaten.setStartcoach(startcoach);
                vertragsdaten.setStartcoachRef(null);
                vertragsdaten.setChangedBy(VALIDATION_SERVICE);
                vertragsdaten.setChangedOn(getLocalDateNow());
                vertragsdatenService.save(vertragsdaten);
                log.info("Successfully replaced startcoach with email {}", startcoach.getEmail());
            }
        }
    }
}
