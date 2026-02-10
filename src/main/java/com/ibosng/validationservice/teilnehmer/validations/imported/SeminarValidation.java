package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbibosservice.entities.*;
import com.ibosng.dbibosservice.entities.smad.SmAd;
import com.ibosng.dbibosservice.services.*;
import com.ibosng.dbibosservice.services.SmAdService;
import com.ibosng.dbibosservice.services.impl.SmAdServiceImpl;
import com.ibosng.dbmapperservice.services.SeminarProjektMapperService;
import com.ibosng.dbservice.entities.*;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarType;
import com.ibosng.dbservice.entities.teilnehmer.*;
import com.ibosng.dbservice.services.*;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.services.MitarbeiterSyncService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Helpers.localDateToString;
import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.validationservice.utils.Parsers.parseStringToInteger;

/**
 * Required for VHS
 */
@Getter
@Setter
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeminarValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {
    private static final String UEBA_WIEN = "ÜBA Wien";
    private static final String UEBA_NOE = "ÜBA NÖ";
    private static final String UEBA_TIROL = "ÜBA Tirol";
    private static final String UEBA_OOE = "ÜBA OÖ";
    private static final String UEBA_SALZBURG = "ÜBA Salzburg";
    private static final String SEMINAR_OBJECT = "seminar";

    private final SeminarService seminarService;
    private final SeminarTypeService seminarTypeService;
    private final SeminarIbosService seminarIbosService;
    private final RgsService rgsService;
    private final BetreuerService betreuerService;
    private final SeminarProjektMapperService seminarProjektMapperService;
    private final ProjektTypeService projektTypeService;
    private final SmAdService smAdService;
    private final AdresseIbosService adresseIbosService;
    private final BenutzerService benutzerService;
    private final BenutzerIbosService benutzerIbosService;
    private final MitarbeiterSyncService mitarbeiterSyncService;
    private final ProjektIbosService projektIbosService;
    private final ProjektPkService projektPkService;
    private final Projekt2ManagerService projekt2ManagerService;
    private final ProjektService projektService;

    private boolean isIdentifierValid;
    private boolean isTypeValid;
    private boolean isStartDateValid;
    private boolean isEndDateValid;
    private boolean isTimeValid;

    @Getter
    @Setter
    private boolean isValid = true;

    public SeminarValidation(SeminarService seminarService,
                             SeminarTypeService seminarTypeService,
                             SeminarIbosService seminarIbosService,
                             RgsService rgsService,
                             BetreuerService betreuerService,
                             SeminarProjektMapperService seminarProjektMapperService,
                             ProjektTypeService projektTypeService,
                             SmAdServiceImpl smAdService,
                             AdresseIbosService adresseIbosService,
                             BenutzerService benutzerService,
                             BenutzerIbosService benutzerIbosService,
                             MitarbeiterSyncService mitarbeiterSyncService,
                             ProjektIbosService projektIbosService,
                             ProjektPkService projektPkService,
                             Projekt2ManagerService projekt2ManagerService,
                             ProjektService projektService
    ) {
        this.seminarService = seminarService;
        this.seminarTypeService = seminarTypeService;
        this.seminarIbosService = seminarIbosService;
        this.rgsService = rgsService;
        this.betreuerService = betreuerService;
        this.seminarProjektMapperService = seminarProjektMapperService;
        this.projektTypeService = projektTypeService;
        this.smAdService = smAdService;
        this.adresseIbosService = adresseIbosService;
        this.benutzerService = benutzerService;
        this.benutzerIbosService = benutzerIbosService;
        this.mitarbeiterSyncService = mitarbeiterSyncService;
        this.projektIbosService = projektIbosService;
        this.projektPkService = projektPkService;
        this.projekt2ManagerService = projekt2ManagerService;
        this.projektService = projektService;
    }

    @Override
    public boolean shouldValidationRun() {
        return !getSources().contains(TeilnehmerSource.TEILNEHMER_CSV) || getSources().contains(TeilnehmerSource.SYNC_SERVICE);
    }

    /**
     * Executes the validation for a given TeilnehmerStaging and maps/updates the Teilnehmer accordingly.
     * This refactored version extracts helper methods to reduce cognitive complexity.
     *
     * @param staging    the staging data for the participant
     * @param teilnehmer the participant entity to be updated
     * @return true if the validation passes; false otherwise
     */
    @Override
    public boolean executeValidation(TeilnehmerStaging staging, Teilnehmer teilnehmer) {
        if (staging.getSource().equals(TeilnehmerSource.EAMS)) {
            return true;
        }
        // Initialize validation flags.
        initializeFlags(staging);

        // Attempt to get a Seminar for the given staging.
        Seminar seminar = getSeminarForTeilnehmer(staging);
        if (seminar != null) {
            processSeminarFound(staging, teilnehmer, seminar);
            return isValid();
        } else {
            if (checkForMassnahmenummer(staging, teilnehmer)) {
                return isValid();
            }
        }
        teilnehmer.addError("seminarBezeichnung", "Ungültiges Seminar angegeben", GlobalUserHolder.IBOSNG_BACKEND);
        return isValid();
    }

    /**
     * Initializes the validation flags based on the TeilnehmerStaging data.
     */
    private void initializeFlags(TeilnehmerStaging staging) {
        setValid(true);
        setIdentifierValid(!isNullOrBlank(staging.getSeminarIdentifier()));
        setTypeValid(!isNullOrBlank(staging.getSeminarType()));
        setStartDateValid(!isNullOrBlank(staging.getSeminarStartDate()) && isValidDate(staging.getSeminarStartDate()));
        setEndDateValid(!isNullOrBlank(staging.getSeminarEndDate()) && isValidDate(staging.getSeminarEndDate()));
        setTimeValid(!isNullOrBlank(staging.getSeminarStartTime()) && isValidTime(staging.getSeminarStartTime()));
    }

    /**
     * Processes the case where a seminar is found.
     */
    private void processSeminarFound(TeilnehmerStaging staging, Teilnehmer teilnehmer, Seminar seminar) {
        if (!teilnehmer.getSeminars().contains(seminar)) {
            Teilnehmer2Seminar t2s = findTeilnehmer2Seminar(
                    staging,
                    teilnehmer.getTeilnehmerSeminars().stream()
                            .filter(t -> t.getStatus().equals(TeilnehmerStatus.INVALID))
                            .toList()
            );
            if (t2s == null) {
                t2s = teilnehmer.addSeminar(seminar);
                t2s.setCreatedBy(VALIDATION_SERVICE);
                updateTeilnahmeDatesIfMissing(staging, seminar);
            }
            t2s.setSeminar(seminar);
            t2s.setImportFilename(staging.getImportFilename());
            setTeilnehmer2SeminarFields(t2s, staging);
            t2s.setStatus(TeilnehmerStatus.VALID);
        } else {
            Teilnehmer2Seminar alreadyExisting = teilnehmer.getTeilnehmerSeminars().stream()
                    .filter(s -> s.getSeminar() != null && s.getSeminar().equals(seminar))
                    .findFirst()
                    .orElse(null);
            if (alreadyExisting != null) {
                setTeilnehmer2SeminarFields(alreadyExisting, staging);
            }
        }
        // Update staging with seminar details.
        staging.setSeminarIdentifier(seminar.getIdentifier());
        staging.setSeminarType(seminar.getBezeichnung());
        if (seminar.getProject() != null) {
            updateUebaFlag(teilnehmer, seminar);
        }
    }

    /**
     * Updates the teilnahmeVon and teilnahmeBis fields of the staging if they are missing.
     */
    private void updateTeilnahmeDatesIfMissing(TeilnehmerStaging staging, Seminar seminar) {
        if (isNullOrBlank(staging.getTeilnahmeVon()) && seminar.getStartDate() != null) {
            staging.setTeilnahmeVon(localDateToString(seminar.getStartDate()));
        }
        if (isNullOrBlank(staging.getTeilnahmeBis()) && seminar.getEndDate() != null) {
            staging.setTeilnahmeBis(localDateToString(seminar.getEndDate()));
        }
    }

    /**
     * Checks if the seminar's project has one of the Ueba types and sets the flag on the participant.
     *
     * @param teilnehmer
     * @param seminar
     */
    private void updateUebaFlag(Teilnehmer teilnehmer, Seminar seminar) {
        List<ProjektType> projektTypesUeba = new ArrayList<>();
        addProjektType(UEBA_WIEN, projektTypesUeba);
        addProjektType(UEBA_NOE, projektTypesUeba);
        addProjektType(UEBA_OOE, projektTypesUeba);
        addProjektType(UEBA_TIROL, projektTypesUeba);
        addProjektType(UEBA_SALZBURG, projektTypesUeba);
        if (seminar.getProject().getProjektType() != null &&
                projektTypesUeba.contains(seminar.getProject().getProjektType())) {
            teilnehmer.setUeba(true);
        }
    }

    private void addProjektType(String name, List<ProjektType> projektTypeList) {
        ProjektType projektType = projektTypeService.findByName(name);
        if (projektType != null) {
            projektTypeList.add(projektType);
        }
    }

    private boolean checkForMassnahmenummer(TeilnehmerStaging staging, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(staging.getMassnahmennummer())) {
            Teilnehmer2Seminar t2s = findTeilnehmer2Seminar(staging, teilnehmer.getTeilnehmerSeminars());
            if (t2s == null) {
                t2s = new Teilnehmer2Seminar();
                t2s.setTeilnehmer(teilnehmer);
                t2s.setCreatedBy(GlobalUserHolder.IBOSNG_BACKEND);
                t2s.setStatus(TeilnehmerStatus.INVALID);
                teilnehmer.getTeilnehmerSeminars().add(t2s);
            }
            t2s.setImportFilename(staging.getImportFilename());
            setTeilnehmer2SeminarFields(t2s, staging);
            return t2s.getStatus().equals(TeilnehmerStatus.VALID);
        }
        return false;
    }

    private Teilnehmer2Seminar findTeilnehmer2Seminar(TeilnehmerStaging staging,
                                                      List<Teilnehmer2Seminar> t2sList) {
        for (Teilnehmer2Seminar t2s : t2sList) {
            boolean sameMassnahmennummer = !isNullOrBlank(t2s.getMassnahmennummer())
                    && !isNullOrBlank(staging.getMassnahmennummer())
                    && t2s.getMassnahmennummer().equals(staging.getMassnahmennummer());
            if (sameMassnahmennummer) {
                return t2s;
            }
        }
        return null;
    }

    private boolean executeValidationStep(AbstractValidation<TeilnehmerStaging, Teilnehmer2Seminar> validation,
                                          TeilnehmerStaging staging,
                                          Teilnehmer2Seminar t2s,
                                          boolean combineResult) {
        validation.setSources(getSources());
        if (validation.shouldValidationRun()) {
            boolean result = validation.executeValidation(staging, t2s);
            return !combineResult || result;
        }
        return true;
    }

    private void setTeilnehmer2SeminarFields(Teilnehmer2Seminar t2s, TeilnehmerStaging staging) {
        boolean result = executeValidationStep(new EinAustrittsdatumValidation(), staging, t2s, true);
        executeValidationStep(new TeilnahmeVonBisValidation(), staging, t2s, false);
        result = result && executeValidationStep(new ZubuchungValidation(), staging, t2s, true);
        result = result && executeValidationStep(new RgsValidation(rgsService), staging, t2s, true);
        result = result && executeValidationStep(new GeplantValidation(), staging, t2s, true);
        result = result && executeValidationStep(new BetreuerValidation(betreuerService), staging, t2s, true);
        setValid(isValid() && result);

        if (!isNullOrBlank(staging.getBuchungsstatus())) {
            t2s.setBuchungsstatus(staging.getBuchungsstatus());
        }
        if (!isNullOrBlank(staging.getAnmerkung())) {
            t2s.setAnmerkung(staging.getAnmerkung());
        }
        if (!isNullOrBlank(staging.getMassnahmennummer())) {
            t2s.setMassnahmennummer(staging.getMassnahmennummer());
        }
        if (!isNullOrBlank(staging.getVeranstaltungsnummer())) {
            t2s.setVeranstaltungsnummer(staging.getVeranstaltungsnummer());
        }

        if (t2s.getSeminar() != null) {
            if (t2s.getTeilnahmeVon() == null && t2s.getSeminar().getStartDate() != null) {
                t2s.setTeilnahmeVon(t2s.getSeminar().getStartDate());
            }
            if (t2s.getTeilnahmeBis() == null && t2s.getSeminar().getEndDate() != null) {
                t2s.setTeilnahmeBis(t2s.getSeminar().getEndDate());
            }
        }

        if (!isNullOrBlank(staging.getTeilnahmeVon()) && isValidDate(staging.getTeilnahmeVon())) {
            t2s.setTeilnahmeVon(parseDate(staging.getTeilnahmeVon()));
        }
        if (!isNullOrBlank(staging.getTeilnahmeBis()) && isValidDate(staging.getTeilnahmeBis())) {
            t2s.setTeilnahmeBis(parseDate(staging.getTeilnahmeBis()));
        }
    }

    // --- Methods for retrieving the Seminar ---

    private Seminar getSeminarForTeilnehmer(TeilnehmerStaging staging) {
        Set<String> identifiers = getIdentifiers(staging);
        Integer seminarNumber = parseStringToInteger(staging.getSeminarIdentifier());
        // Try to find in the primary (ibosNG) database.
        Seminar seminar = findSeminarInIbosNg(staging, identifiers, seminarNumber);
        // If not found, try to find in the IBOS database.
        if (seminar == null) {
            seminar = findSeminarInIbosDb(staging, identifiers, seminarNumber);
        }
        return seminar;
    }

    private Seminar findSeminarInIbosNg(TeilnehmerStaging teilnehmerStaging, Set<String> identifiers, Integer seminarNumber) {
        List<Seminar> seminars = new ArrayList<>();
        if (seminarNumber != null) { // Try to find by seminar number
            seminars = seminarService.findAllBySeminarNummer(seminarNumber);
        } else if (!identifiers.isEmpty() && isValidDate((teilnehmerStaging.getSeminarStartDate())) && isValidDate(teilnehmerStaging.getSeminarEndDate()) && isValidTime(teilnehmerStaging.getSeminarStartTime())) {
            seminars = seminarService.findSeminarByDetails(
                    identifiers,
                    parseDate(teilnehmerStaging.getSeminarStartDate()),
                    parseDate(teilnehmerStaging.getSeminarEndDate()),
                    parseTime(teilnehmerStaging.getSeminarStartTime())
            );
        }
        Seminar seminar = findFirstObject(seminars, identifiers, SEMINAR_OBJECT);

        if (seminar == null && isIdentifierValid()) {
            seminars = seminarService.findAllByIdentifier(teilnehmerStaging.getSeminarIdentifier());
            seminar = findFirstObject(seminars, new HashSet<>(Collections.singletonList(teilnehmerStaging.getSeminarIdentifier())), SEMINAR_OBJECT);
            if (seminar == null) {
                seminars = seminarService.findByBezeichnung(teilnehmerStaging.getSeminarIdentifier());
                seminar = findFirstObject(seminars, new HashSet<>(Collections.singletonList(teilnehmerStaging.getSeminarIdentifier())), SEMINAR_OBJECT);
            }
            if (seminar == null && isStartDateValid() && isEndDateValid()) {
                SeminarType seminarType = seminarTypeService.findByName(teilnehmerStaging.getSeminarType());
                seminar = seminarService.findByIdentifierAndSeminarTypeAndStartDateAndEndDateAndStartTime(
                        teilnehmerStaging.getSeminarIdentifier(),
                        seminarType,
                        parseDate(teilnehmerStaging.getSeminarStartDate()),
                        parseDate(teilnehmerStaging.getSeminarEndDate()),
                        parseTime(teilnehmerStaging.getSeminarStartTime())
                );
            }
        }
        return seminar;
    }

    private Seminar findSeminarInIbosDb(TeilnehmerStaging teilnehmerStaging, Set<String> identifiers, Integer seminarNumber) {
        Seminar seminar = null;
        SeminarIbos seminarIbos = null;
        if (seminarNumber != null) {
            Optional<SeminarIbos> optionalSeminarIbos = seminarIbosService.findById(seminarNumber);
            if (optionalSeminarIbos.isPresent()) {
                seminarIbos = optionalSeminarIbos.get();
            }
        }
        if (seminarIbos == null && !identifiers.isEmpty()) {
            List<SeminarIbos> seminarsIbos = seminarIbosService.findSeminarByDetails(
                    identifiers,
                    parseDate(teilnehmerStaging.getSeminarStartDate()),
                    parseDate(teilnehmerStaging.getSeminarEndDate()),
                    parseTime(teilnehmerStaging.getSeminarStartTime())
            );
            seminarIbos = findFirstObject(seminarsIbos, identifiers, "seminarIbos");
        }
        if (seminarIbos != null) {
            seminar = seminarProjektMapperService.mapSeminarIbosToSeminar(seminarIbos);
            var seminarsNg = seminarService.findAllBySeminarNummer(seminar.getSeminarNummer());
            if (seminarsNg.isEmpty()) {
                seminar.setStatus(Status.ACTIVE);
                seminar.setCreatedBy(VALIDATION_SERVICE);
                seminar = seminarService.save(seminar);
                setTrainersForSeminar(seminar, seminarIbos);
                setManagersForProjekt(seminar.getProject());
            } else {
                seminar = findFirstObject(seminarsNg, identifiers, "seminar");
            }
        }
        return seminar;
    }

    private void setManagersForProjekt(Projekt projekt) {
        ProjektIbos projektIbos = projektIbosService.findById(projekt.getProjektNummer()).orElse(null);
        List<ProjektPk> projektPks = projektPkService.findByProjectId(projektIbos.getId());
        List<Projekt2Manager> projekt2Managers = new ArrayList<>();
        for (ProjektPk projektPk : projektPks) {
            if (projektPk == null) {
                log.info("Cannot find a projekt with projektNummer: {}", projekt.getProjektNummer());
                continue;
            }
            AdresseIbos adresseIbos = adresseIbosService.findById(projektPk.getAdresseId()).orElse(null);
            if (adresseIbos == null) {
                log.error("No AdresseIbos entity found for id {}", projektPk.getAdresseId());
                continue;
            }

            Benutzer benutzer = syncMAFromAdresseIbos(adresseIbos);
            if (benutzer == null) {
                log.info("Cannot find user from upn: {}", adresseIbos.getAdadnr());
                continue;
            }
            Projekt2Manager projekt2Manager = seminarProjektMapperService.mapProjektPk(projektPk, benutzer, projekt);
            projekt2Manager = projekt2ManagerService.save(projekt2Manager);
            projekt2Managers.add(projekt2Manager);
        }
        projekt.setTrainerSeminars(projekt2Managers);
        projektService.save(projekt);
    }

    private void setTrainersForSeminar(Seminar seminar, SeminarIbos seminarIbos) {
        //Sync and set Seminar2Trainers
        List<SmAd> currentSeminarSmAds = smAdService.findAllBySeminarNummer(seminarIbos.getId());

        List<Seminar2Trainer> seminars2trainers = new ArrayList<>();
        log.info("Go through smAd entries {} for seminar: {}", currentSeminarSmAds.size(), seminar.getBezeichnung());
        for (SmAd smAd : currentSeminarSmAds) {
            Optional<AdresseIbos> trainer = adresseIbosService.findById(smAd.getAdresseAdadnr());
            if (trainer.isPresent()) {
                log.info("Trainer is present in old Database {}", trainer.get().getAdadnr());
                Benutzer benutzer = syncMAFromAdresseIbos(trainer.get());
                if (benutzer == null) {
                    continue;
                }
                log.info("Benutzer found for trainer: {}", benutzer);
                Seminar2Trainer mappedEntity = seminarProjektMapperService.mapSMADtoSeminar2Trainer(smAd, seminar, benutzer);
                log.info("Mapped Seminar2Trainer entity: {}", mappedEntity.toString());
                boolean exists = seminar.getTrainerSeminars().stream()
                        .anyMatch(existing -> existing.getTrainer().getId().equals(mappedEntity.getTrainer().getId()));
                if (!exists) {
                    seminars2trainers.add(mappedEntity);
                } else {
                    log.info("Trainer {} already mapped to seminar {}", benutzer.getId(), seminar);
                }
            }
        }
        seminar.setTrainerSeminars(seminars2trainers);
    }

    private Benutzer syncMAFromAdresseIbos(AdresseIbos adresseIbos) {
        BenutzerIbos benutzerIbos = benutzerIbosService.findBenutzerByBnadnr(adresseIbos.getAdadnr());
        if (benutzerIbos != null) {
            Benutzer existingBenutzer = benutzerService.findByUpnContaining(benutzerIbos.getBnupn());
            if (existingBenutzer != null) {
                log.info("Benutzer already exists for trainer: {}", existingBenutzer);
                return existingBenutzer;
            }
            mitarbeiterSyncService.syncMitarbeiterFromIbisacamWithUPN(benutzerIbos.getBnupn(), null, null);
            // Re-fetch user after external sync
            existingBenutzer = benutzerService.findByUpn(benutzerIbos.getBnupn());
            log.info("Created new Benutzer for trainer: {}", benutzerIbos.getBnupn());
            if (existingBenutzer == null) {
                log.error("Trainer {} was not found even after sync!", benutzerIbos.getBnupn());
                return null;
            }
        }
        log.error("No BenutzerIbos was found in ibosAlt for AdresseIbos: {}", adresseIbos.getAdadnr());
        return null;
    }

    private Set<String> getIdentifiers(TeilnehmerStaging staging) {
        Set<String> identifiers = new HashSet<>();
        if (isTypeValid) {
            identifiers.addAll(Arrays.asList(staging.getSeminarType().split("\\s+")));
        }
        if (isIdentifierValid) {
            identifiers.addAll(Arrays.asList(staging.getSeminarIdentifier().split("\\s+")));
        }
        return identifiers;
    }

}
