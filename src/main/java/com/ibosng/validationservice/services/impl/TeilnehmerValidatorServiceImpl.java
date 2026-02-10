package com.ibosng.validationservice.services.impl;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbibosservice.services.SeminarIbosService;
import com.ibosng.dbibosservice.services.SeminarTeilnehmerIbosService;
import com.ibosng.dbmapperservice.services.TeilnehmerMapperService;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.TeilnehmerStagingService;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.dbservice.utils.Parsers;
import com.ibosng.validationservice.services.TeilnehmerValidatorService;
import com.ibosng.validationservice.services.ValidatorService;
import com.ibosng.validationservice.validations.NachnameValidation;
import com.ibosng.validationservice.validations.SVNValidation;
import com.ibosng.validationservice.validations.VornameValidation;
import com.ibosng.workflowservice.dtos.WorkflowPayload;
import com.ibosng.workflowservice.enums.SWorkflowGroups;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.ibosng.workflowservice.services.WFStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.validationservice.utils.Parsers.isLong;
import static com.ibosng.validationservice.utils.Parsers.parseStringToLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeilnehmerValidatorServiceImpl implements TeilnehmerValidatorService {

    private final TeilnehmerService teilnehmerService;
    private final TeilnehmerMapperService teilnehmerMapperService;
    private final TeilnehmerStagingService teilnehmerStagingService;
    private final SeminarTeilnehmerIbosService seminarTeilnehmerService;
    private final SeminarIbosService seminarIbosService;
    private final WFStartService wfStartService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final ManageWFsService manageWFsService;
    private final ManageWFItemsService manageWFItemsService;
    private final ValidatorService validatorService;

    @Override
    public Teilnehmer getTeilnehmer(TeilnehmerStaging teilnehmerStaging) {
        Teilnehmer teilnehmer = null;
        if (!isTeilnehmerUniqueIdentified(teilnehmerStaging.getVorname(), teilnehmerStaging.getNachname(), teilnehmerStaging.getSvNummer(), teilnehmerStaging.getGeburtsdatum())) {
            log.warn("Unsufficient or invalid data for the teilnehmer: vorname {}, nachname {}, svnr {}", teilnehmerStaging.getVorname(), teilnehmerStaging.getNachname(), teilnehmerStaging.getSvNummer());
            return teilnehmer;
        }
        if (teilnehmerStaging.getSource().equals(TeilnehmerSource.MANUAL) && teilnehmerStaging.getTeilnehmerId() != 0) {
            Optional<Teilnehmer> optionalTeilnehmer = teilnehmerService.findById(teilnehmerStaging.getTeilnehmerId());
            if (optionalTeilnehmer.isPresent()) {
                teilnehmer = optionalTeilnehmer.get();
                clearTeilnehmerDataStatuses(teilnehmer);
                teilnehmer.setImportFilename(teilnehmerStaging.getImportFilename());
                return teilnehmer;
            }
        }
        if (isSvnNotNullAndValid(teilnehmerStaging.getGeburtsdatum(), teilnehmerStaging.getSvNummer())) {
            List<Teilnehmer> teilnehmers = teilnehmerService.getBySVN(teilnehmerStaging.getSvNummer());
            teilnehmer = findFirstObject(teilnehmers, new HashSet<>(Collections.singletonList(teilnehmerStaging.getSvNummer())), "teilnehmer");
        }

        if (teilnehmer == null && isValidTeilnehmerWithSvNummer(teilnehmerStaging)) {
            List<Teilnehmer> teilnehmers = teilnehmerService.findAllByVornameAndNachnameAndSvNummer(teilnehmerStaging.getVorname(), teilnehmerStaging.getNachname(), teilnehmerStaging.getSvNummer());
            teilnehmer = findFirstObject(teilnehmers, new HashSet<>(List.of(teilnehmerStaging.getVorname(), teilnehmerStaging.getNachname())), "teilnehmer");
        }

        if (teilnehmer == null && isValidTeilnehmerWithGeburtsdatum(teilnehmerStaging)) {
            List<Teilnehmer> teilnehmers = teilnehmerService.findAllByVornameAndNachnameAndGeburtsdatum(teilnehmerStaging.getVorname(), teilnehmerStaging.getNachname(), parseDate(teilnehmerStaging.getGeburtsdatum()));
            teilnehmer = findFirstObject(teilnehmers, new HashSet<>(List.of(teilnehmerStaging.getVorname(), teilnehmerStaging.getNachname())), "teilnehmer");
        }
        if (!TeilnehmerSource.EAMS.equals(teilnehmerStaging.getSource())) {
            if (teilnehmer == null) {
                teilnehmer = createNewTeilnehmer();
                teilnehmer.setImportFilename(teilnehmerStaging.getImportFilename());
                teilnehmer.setInfo(teilnehmerStaging.getInfo());
            } else {
                clearTeilnehmerDataStatuses(teilnehmer);
                teilnehmer.setImportFilename(teilnehmerStaging.getImportFilename());
            }
        }
        return teilnehmer;
    }

    private boolean isValidTeilnehmerWithSvNummer(TeilnehmerStaging teilnehmerStaging) {
        return VornameValidation.isVornameValid(teilnehmerStaging.getVorname()) &&
                NachnameValidation.isNachnameValid(teilnehmerStaging.getNachname()) &&
                !isNullOrBlank(teilnehmerStaging.getSvNummer()) &&
                isLong(teilnehmerStaging.getSvNummer());
    }

    private boolean isValidTeilnehmerWithGeburtsdatum(TeilnehmerStaging teilnehmerStaging) {
        return VornameValidation.isVornameValid(teilnehmerStaging.getVorname()) &&
                NachnameValidation.isNachnameValid(teilnehmerStaging.getNachname()) &&
                !isNullOrBlank(teilnehmerStaging.getGeburtsdatum()) &&
                Parsers.isValidDate(teilnehmerStaging.getGeburtsdatum());
    }

    @Override
    public Teilnehmer getTeilnehmerForManual(TeilnehmerDto teilnehmerDto) {
        Teilnehmer teilnehmer = getTeilnehmerFromSVNOrNames(teilnehmerDto);
        if (teilnehmer == null) {
            teilnehmer = createNewTeilnehmer();
        } else {
            clearTeilnehmerDataStatuses(teilnehmer);
        }
        return teilnehmer;
    }

    @Override
    public Teilnehmer getTeilnehmerByTeilnehmerId(int teilnehmerId) {
        return teilnehmerService.findById(teilnehmerId)
                .map(t -> {
                    clearTeilnehmerDataStatuses(t);
                    return t;
                })
                .orElseGet(this::createNewTeilnehmer);
    }

    @Override
    public Teilnehmer getTeilnehmerFromSVNOrNames(TeilnehmerDto teilnehmerDto) {
        Teilnehmer teilnehmer = null;

        if (teilnehmerDto.getId() != 0) {
            Optional<Teilnehmer> optionalTeilnehmer = teilnehmerService.findById(teilnehmerDto.getId());
            if (optionalTeilnehmer.isPresent()) {
                teilnehmer = optionalTeilnehmer.get();
                clearTeilnehmerDataStatuses(teilnehmer);
                return teilnehmer;
            }
        }
        if (!isTeilnehmerUniqueIdentified(teilnehmerDto.getVorname(), teilnehmerDto.getNachname(), teilnehmerDto.getSvNummer(), teilnehmerDto.getGeburtsdatum())) {
            log.warn("Unsufficient or invalid data for the teilnehmer: vorname {}, nachname {}, svnr {}", teilnehmerDto.getVorname(), teilnehmerDto.getNachname(), teilnehmerDto.getSvNummer());
            return teilnehmer;
        }
        if (isSvnNotNullAndValid(teilnehmerDto.getGeburtsdatum(), teilnehmerDto.getSvNummer())) {
            List<Teilnehmer> teilnehmers = teilnehmerService.getBySVN(teilnehmerDto.getSvNummer());
            if (!teilnehmers.isEmpty() && teilnehmerDto.getId() == 0) {
                teilnehmerDto.getErrorsMap().put("svNummer", "SVN bereits vorhanden");
                teilnehmerDto.setErrors(new ArrayList<>(teilnehmerDto.getErrorsMap().keySet()));
                return null;
            }
            teilnehmer = findFirstObject(teilnehmers, new HashSet<>(Collections.singletonList(teilnehmerDto.getSvNummer())), "teilnehmer");
        }

        if (teilnehmer == null && VornameValidation.isVornameValid(teilnehmerDto.getVorname()) && NachnameValidation.isNachnameValid(teilnehmerDto.getNachname())) {
            List<Teilnehmer> teilnehmers = teilnehmerService.getByVornameAndNachname(teilnehmerDto.getVorname(), teilnehmerDto.getNachname());
            teilnehmer = findFirstObject(teilnehmers, new HashSet<>(List.of(teilnehmerDto.getVorname(), teilnehmerDto.getNachname())), "teilnehmer");
        }
        return teilnehmer;
    }

    @Override
    public Teilnehmer getTeilnehmerForZeiterfassung(TeilnehmerDto teilnehmerDto) {
        Teilnehmer teilnehmer = null;
        if (!isTeilnehmerUniqueIdentified(teilnehmerDto.getVorname(), teilnehmerDto.getNachname(), teilnehmerDto.getSvNummer(), teilnehmerDto.getGeburtsdatum())) {
            log.warn("Unsufficient or invalid data for the teilnehmer: vorname {}, nachname {}, svnr {}", teilnehmerDto.getVorname(), teilnehmerDto.getNachname(), teilnehmerDto.getSvNummer());
            return teilnehmer;
        }
        if (isSvnNotNullAndValid(teilnehmerDto.getGeburtsdatum(), teilnehmerDto.getSvNummer())) {
            List<Teilnehmer> teilnehmers = teilnehmerService.getBySVN(teilnehmerDto.getSvNummer());
            if (!teilnehmers.isEmpty() && teilnehmerDto.getId() == 0) {
                teilnehmer = findFirstObject(teilnehmers, new HashSet<>(Collections.singletonList(teilnehmerDto.getSvNummer())), "teilnehmer");
            }
        }

        if (teilnehmer == null && VornameValidation.isVornameValid(teilnehmerDto.getVorname()) && NachnameValidation.isNachnameValid(teilnehmerDto.getNachname())) {
            List<Teilnehmer> teilnehmers = teilnehmerService.getByVornameAndNachname(teilnehmerDto.getVorname(), teilnehmerDto.getNachname());
            teilnehmer = findFirstObject(teilnehmers, new HashSet<>(List.of(teilnehmerDto.getVorname(), teilnehmerDto.getNachname())), "teilnehmer");
        }
        return teilnehmer;
    }

    private void clearTeilnehmerDataStatuses(Teilnehmer teilnehmer) {
        teilnehmer.setErrors(new ArrayList<>());
        teilnehmer.setChangedOn(LocalDateTime.now());
    }

    private boolean isSvnValid(String geburtsdatum, String svn) {
        LocalDate geburtsdatumDate = null;
        if (geburtsdatum != null && isValidDate(geburtsdatum)) {
            geburtsdatumDate = parseDate(geburtsdatum);
        }
        return SVNValidation.validateSVN(svn, geburtsdatumDate) != null;
    }

    private Teilnehmer createNewTeilnehmer() {
        Teilnehmer teilnehmer = new Teilnehmer();
        teilnehmer.setAdresse(new Adresse());
        teilnehmer.getAdresse().setStatus(Status.NEW);
        teilnehmer.getAdresse().setCreatedBy(VALIDATION_SERVICE);
        teilnehmer.setErrors(new ArrayList<>());
        teilnehmer.setStatus(TeilnehmerStatus.NEW);
        teilnehmer.setCreatedBy(VALIDATION_SERVICE);
        return teilnehmer;
    }

    private boolean isSvnNotNullAndValid(String geburtsdatum, String svn) {
        return !isNullOrBlank(svn) && isSvnValid(geburtsdatum, svn);
    }

    private boolean isTeilnehmerUniqueIdentified(String vorname, String nachname, String svn, String geburtsdatum) {
        if (VornameValidation.isVornameValid(vorname) && NachnameValidation.isNachnameValid(nachname) && !isNullOrBlank(svn)) {
            return true;
        } else if (VornameValidation.isVornameValid(vorname) && NachnameValidation.isNachnameValid(nachname) && isValidDate(geburtsdatum)) {
            return true;
        }
        return false;
    }

    @Override
    public void mapTeilnehmerIbosToTeilnehmer(Set<AdresseIbos> adressesIbos, Integer seminarNummer) {
        String identifier = "IMPORTING_MISSING_TN-" + getLocalDateNow();
        WWorkflowGroup wWorkflowGroup = wfStartService.createWorkflowGroupAndInstances(SWorkflowGroups.IMPORT_TEILNEHMER_FILES.getValue(), identifier, VALIDATION_SERVICE);
        wWorkflowGroup.setStatus(WWorkflowStatus.IN_PROGRESS);
        wWorkflowGroupService.save(wWorkflowGroup);
        WWorkflow wWorkflow = manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.IMPORT_TEILNEHMER, WWorkflowStatus.IN_PROGRESS, VALIDATION_SERVICE);
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.IMPORTING_TEILNEHMER, WWorkflowStatus.IN_PROGRESS, VALIDATION_SERVICE);
        for (AdresseIbos adresseIbos : adressesIbos) {
            TeilnehmerStaging teilnehmerStaging = createTeilnehmerStagingWithMapping(identifier, adresseIbos);
            SeminarIbos seminarIbos = seminarIbosService.findById(seminarNummer).orElse(null);
            if (seminarIbos != null) {
                SeminarTeilnehmerIbos seminarTeilnehmerIbos = seminarTeilnehmerService.findByAdresseAndSeminar(adresseIbos.getAdadnr(), seminarNummer);
                if (seminarTeilnehmerIbos != null) {
                    teilnehmerMapperService.mapSeminarDataToTeilnehmerStaging(teilnehmerStaging, seminarTeilnehmerIbos, seminarIbos);
                    TeilnehmerStaging savedTeilnehmerStaging = teilnehmerStagingService.save(teilnehmerStaging);
                    log.info("Persisted multiple TeilnehmerStaging: " + savedTeilnehmerStaging.toString());
                } else {
                    TeilnehmerStaging savedTeilnehmerStaging = teilnehmerStagingService.save(teilnehmerStaging);
                    log.info("Persisted TeilnehmerStaging: {}", savedTeilnehmerStaging.toString());
                }
            } else {
                TeilnehmerStaging savedTeilnehmerStaging = teilnehmerStagingService.save(teilnehmerStaging);
                log.info("Persisted TeilnehmerStaging: {}", savedTeilnehmerStaging.toString());
            }
        }
        manageWFItemsService.setWFItemStatus(wWorkflow, SWorkflowItems.IMPORTING_TEILNEHMER, WWorkflowStatus.COMPLETED, VALIDATION_SERVICE);
        WorkflowPayload workflowPayload = new WorkflowPayload(wWorkflow.getId(), identifier);
        validatorService.validateImportedParticipants(workflowPayload);
    }

    private TeilnehmerStaging createTeilnehmerStagingWithMapping(String identifier, AdresseIbos adresseIbos) {
        TeilnehmerStaging teilnehmerStaging = teilnehmerMapperService.adresseIbosToTeilnehmerStagingDirect(adresseIbos);
        teilnehmerStaging.setImportFilename(identifier);
        teilnehmerStaging.setSource(TeilnehmerSource.SYNC_SERVICE);
        teilnehmerStaging.setStatus(TeilnehmerStatus.NEW);
        return teilnehmerStaging;
    }
}
