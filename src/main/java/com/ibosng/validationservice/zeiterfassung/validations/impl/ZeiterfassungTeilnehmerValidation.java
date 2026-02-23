package com.ibosng.validationservice.zeiterfassung.validations.impl;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.entities.ksttn.KsttnStatus;
import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbibosservice.entities.teilnahme.Teilnahme;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbibosservice.services.KsttnStatusService;
import com.ibosng.dbibosservice.services.SeminarTeilnehmerIbosService;
import com.ibosng.dbibosservice.services.TeilnahmeService;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.entities.zeiterfassung.Zeiterfassung;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungReason;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungReasonService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungService;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.services.TeilnehmerValidatorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;

@RequiredArgsConstructor
@Slf4j
@Component
@Transactional(transactionManager = "postgresTransactionManager")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeiterfassungTeilnehmerValidation implements Validation<ZeiterfassungTransferDto, ZeiterfassungTransfer> {

    private static final String FIRMA_IBIS_ACAM_BILDUNGS_GMBH = "ibis acam Bildungs GmbH";
    private final AdresseIbosService adresseIbosService;
    private final TeilnahmeService teilnahmeService;
    private final TeilnehmerValidatorService teilnehmerValidatorService;
    private final ZeiterfassungReasonService zeiterfassungReasonService;
    private final KsttnStatusService ksttnStatusService;
    private final ZeiterfassungService zeiterfassungService;
    private final SeminarTeilnehmerIbosService seminarTeilnehmerService;
    private final TeilnehmerService teilnehmerService;
    private final IbisFirmaService ibisFirmaService;
    private final PersonalnummerService personalnummerService;
    private final GlobalUserHolder globalUserHolder;

    @Autowired
    @Lazy   //avoid circular dependency exception
    private ZeiterfassungTeilnehmerValidation self;    //for avoiding proxy-self-invocation problem

    @Getter
    @Setter
    private String createdBy;

    @Override
    public boolean executeValidation(ZeiterfassungTransferDto zeiterfassungDto, ZeiterfassungTransfer zeiterfassungTransfer) {
        if (!zeiterfassungDto.getErrors().isEmpty()) {
            log.warn("Errors detected in Zeiterfassungtransfer with id {}. Syncing process will not continue.", zeiterfassungTransfer.getId());
            return false;
        }
        log.info("Starting ZeiterfassungTransfer validation for TN for ZeiterfassungTransfer with id {}.", zeiterfassungTransfer.getId());
        for (Seminar seminar : zeiterfassungTransfer.getSeminars()) {
            if (zeiterfassungDto.getErrorsMap().isEmpty() && (zeiterfassungTransfer.getDatumBis() != null) && (zeiterfassungTransfer.getDatumVon() != null)) {
                self.executeForSingleSeminar(zeiterfassungDto, zeiterfassungTransfer, seminar);
            } else {
                return false;
            }
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "postgresTransactionManager")
    public void executeForSingleSeminar(ZeiterfassungTransferDto zeiterfassungDto, ZeiterfassungTransfer zeiterfassungTransfer, Seminar seminar) {
        List<Teilnahme> teilnahmes = teilnahmeService.findBySeminarSmnrAndInPeriod(
                seminar.getSeminarNummer(), zeiterfassungTransfer.getDatumVon(), zeiterfassungTransfer.getDatumBis());

        // Group Teilnahme objects by adresseAdnr
        Map<Integer, List<Teilnahme>> adresse2Teilnahmen = teilnahmes.stream()
                .collect(Collectors.groupingBy(teilnahme -> teilnahme.getId().getAdresseAdnr()));
        importMissingTeilnehmer(adresse2Teilnahmen.keySet(), seminar.getSeminarNummer());
        Set<Teilnehmer> allTeilnehmer = new HashSet<>();
        for (Integer adresseIbosId : adresse2Teilnahmen.keySet()) {
            AdresseIbos adresseIbos = adresseIbosService.findById(adresseIbosId).orElse(null);
            if (adresseIbos == null) {
                continue;
            }
            Teilnehmer teilnehmer = getTeilnehmer(adresseIbos);
            if (teilnehmer != null) {
                allTeilnehmer.add(teilnehmer);
                if (teilnehmer.getPersonalnummer() == null) {
                    createPersonalnummerForTN(adresseIbos, teilnehmer);
                }
                if (!isTeilnehmerContainsSeminar(teilnehmer, seminar)) {
                    createTeilnehmer2Seminar(teilnehmer, seminar, adresseIbos.getAdadnr());
                }
                log.info("Transfering Zeiterfassung for tn with id {}", teilnehmer.getId());
                for (Teilnahme teilnahme : adresse2Teilnahmen.get(adresseIbosId)) {
                    Zeiterfassung mappedZeiterfassung = mapTeilnahme(teilnahme, teilnehmer, seminar, zeiterfassungDto.getErrors().isEmpty());
                    // If no reason found we skip it
                    if (mappedZeiterfassung.getZeiterfassungReason() == null) {
                        continue;
                    }
                    Zeiterfassung existingZeiterfassung = getExistingZeiterfassung(mappedZeiterfassung);
                    if (existingZeiterfassung != null) {
                        mappedZeiterfassung.setId(existingZeiterfassung.getId());
                    }
                    mappedZeiterfassung.setZeiterfassungTransfer(zeiterfassungTransfer);
                    zeiterfassungService.save(mappedZeiterfassung);
                }
            }
        }
        zeiterfassungDto.setTeilnehmerNumber(allTeilnehmer.size());
    }

    public void importMissingTeilnehmer(Set<Integer> adresseIbosIds, Integer seminarNummer) {
        Set<AdresseIbos> missingTeilnehmer = new HashSet<>();
        log.info("Importing missing TN for ZeiterfassungTransfer with TN size {}.", adresseIbosIds.size());
        for (Integer adresseIbosId : adresseIbosIds) {
            AdresseIbos adresseIbos = adresseIbosService.findById(adresseIbosId).orElse(null);
            if (adresseIbos == null) {
                continue;
            }
            Teilnehmer teilnehmer = getTeilnehmer(adresseIbos);
            if (teilnehmer == null) {
                log.warn("Teilnehmer not found for teilnehmerIbos tn: {}", adresseIbos);
                missingTeilnehmer.add(adresseIbos);
            }
        }
        log.info("Finished importing missing TN for ZeiterfassungTransfer");
        if (!missingTeilnehmer.isEmpty()) {
            teilnehmerValidatorService.mapTeilnehmerIbosToTeilnehmer(missingTeilnehmer, seminarNummer);
        }
    }

    private void createTeilnehmer2Seminar(Teilnehmer teilnehmer, Seminar seminar, Integer adresseId) {
        try {
            List<SeminarTeilnehmerIbos> seminarTeilnehmerIbos = seminarTeilnehmerService.findByAdresseNr(adresseId)
                    .stream().filter(sem -> sem.getSmTnId().getSeminarSmnr().equals(seminar.getSeminarNummer())).toList();
            if (seminarTeilnehmerIbos.size() == 1) {
                Teilnehmer2Seminar teilnehmer2Seminar = new Teilnehmer2Seminar();
                teilnehmer2Seminar.setTeilnehmer(teilnehmer);
                teilnehmer2Seminar.setStatus(TeilnehmerStatus.VALID);
                teilnehmer2Seminar.setCreatedBy(globalUserHolder.getUsername());
                teilnehmer.getTeilnehmerSeminars().add(teilnehmer2Seminar);
                teilnehmerService.save(teilnehmer);
            }
        } catch (Exception e) {
            log.error("Error while creating Teilnehmer2Seminar for Teilnehmer {} and Seminar {}. Error: {}", teilnehmer.getId(), seminar.getId(), e.getMessage());
        }
    }

    public boolean isTeilnehmerContainsSeminar(Teilnehmer teilnehmer, Seminar seminar) {
        if ((teilnehmer == null) || (seminar == null)) {
            return false;
        }
        return teilnehmer.getSeminars().stream().anyMatch(s -> s != null && s.getId().equals(seminar.getId()));
    }

    public Zeiterfassung getExistingZeiterfassung(Zeiterfassung zeiterfassung) {
        return zeiterfassungService.findBySeminarTeilnehmerDatum(zeiterfassung.getSeminar(),
                zeiterfassung.getTeilnehmer(), zeiterfassung.getDatum()).stream().findFirst().orElse(null);
    }

    public Zeiterfassung mapTeilnahme(Teilnahme teilnahme, Teilnehmer teilnehmer, Seminar seminar, boolean hasNoErrors) {
        Zeiterfassung zeiterfassung = new Zeiterfassung();
        zeiterfassung.setZeiterfassungReason(getZeiterfassungReason(teilnahme));
        zeiterfassung.setTeilnehmer(teilnehmer);
        zeiterfassung.setSeminar(seminar);
        zeiterfassung.setDatumBis(teilnahme.getTnDatumBis());
        zeiterfassung.setDatum(teilnahme.getId().getDatum());
        if (hasNoErrors) {
            zeiterfassung.setStatus(ZeiterfassungStatus.VALID);
        } else {
            zeiterfassung.setStatus(ZeiterfassungStatus.INVALID);
        }
        zeiterfassung.setCreatedBy(createdBy);
        return zeiterfassung;
    }

    public ZeiterfassungReason getZeiterfassungReason(Teilnahme teilnahme) {
        if (teilnahme.getKsttnStatusV() == null) {
            return null;
        }
        ZeiterfassungReason zeiterfassungReason = null;
        KsttnStatus ksttnStatus = ksttnStatusService.findByTeilnahme(teilnahme);
        if (ksttnStatus != null) {
            zeiterfassungReason = zeiterfassungReasonService.findByBezeichnungAndShortBezeichnung(ksttnStatus.getKsttnbez(), ksttnStatus.getKsttnkuerzel());
            if (zeiterfassungReason == null) {
                zeiterfassungReason = zeiterfassungReasonService.findAllByIbosId(ksttnStatus.getKsttnkyindex()).stream().findFirst().orElse(null);
            }
        }
        return zeiterfassungReason;
    }

    public Teilnehmer getTeilnehmer(AdresseIbos adresseIbos) {
        TeilnehmerDto teilnehmerDto = new TeilnehmerDto();
        teilnehmerDto.setNachname(adresseIbos.getAdznf1());
        teilnehmerDto.setVorname(adresseIbos.getAdvnf2());
        teilnehmerDto.setGeburtsdatum(adresseIbos.getAdgebdatum().toString());
        teilnehmerDto.setSvNummer(adresseIbos.getAdsvnr().replaceAll("\\s+", ""));
        return teilnehmerValidatorService.getTeilnehmerForZeiterfassung(teilnehmerDto);
    }

    private void createPersonalnummerForTN(AdresseIbos adresseIbos, Teilnehmer teilnehmer) {
        AdresseIbos adresseIbosWithPN = null;
        if (isNullOrBlank(adresseIbos.getAdpersnr())) {
            log.warn("No personalnummer found for TN with adresseID {} in ibos DB", adresseIbos.getAdadnr());
            List<AdresseIbos> adresseIbosList = adresseIbosService.findAllByAdemail1(adresseIbos.getAdemail1()).stream().filter(adresse -> !isNullOrBlank(adresse.getAdpersnr())).toList();
            if (adresseIbosList.isEmpty()) {
                log.warn("No personalnummer found for TN with email {}, trying to find with first and last name", adresseIbos.getAdemail1());
                adresseIbosList = adresseIbosService.findAllByVornameAndNachname(adresseIbos.getAdvnf2(), adresseIbos.getAdznf1()).stream().filter(adresse -> !isNullOrBlank(adresse.getAdpersnr())).toList();
            }
            if (adresseIbosList.size() == 1) {
                adresseIbosWithPN = adresseIbosList.get(0);
            } else if (adresseIbosList.size() > 1) {
                log.warn("Multiple entries in ADRESSE with PN found for TN with email {}", adresseIbos.getAdemail1());
                adresseIbosWithPN = adresseIbosList.get(0);
            }
        } else {
            adresseIbosWithPN = adresseIbos;
        }
        if (adresseIbosWithPN != null) {
            log.info("Personalnummer found for TN with adresseID {} in ibos DB", adresseIbosWithPN.getAdadnr());
            Personalnummer personalnummer = new Personalnummer();
            IbisFirma ibisFirma = ibisFirmaService.findByName(FIRMA_IBIS_ACAM_BILDUNGS_GMBH);
            if (ibisFirma != null) {
                personalnummer.setFirma(ibisFirma);
            }
            personalnummer.setMitarbeiterType(MitarbeiterType.TEILNEHMER);
            personalnummer.setCreatedBy(VALIDATION_SERVICE);
            personalnummer.setStatus(Status.NEW);
            personalnummer.setIsIbosngOnboarded(false);
            personalnummer.setOnboardedOn(getLocalDateNow());
            personalnummer.setPersonalnummer(adresseIbosWithPN.getAdpersnr());
            personalnummer = personalnummerService.save(personalnummer);
            teilnehmer.setPersonalnummer(personalnummer);
            teilnehmerService.save(teilnehmer);
            log.info("Personalnummer created and assigned to TN with ID {}", teilnehmer.getId());
        } else {
            log.warn("No personalnummer found for TN with email {} in all ADRESSE entries, TN Abwesenheiten will not be send to LHR, ", adresseIbos.getAdemail1());
        }

    }

}
