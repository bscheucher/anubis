package com.ibosng.usercreationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowGroup;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.ZusatzInfoService;
import com.ibosng.dbservice.services.mitarbeiter.*;
import com.ibosng.dbservice.services.workflows.WWorkflowGroupService;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.ibosng.usercreationservice.dto.UserAnlageResponseDto;
import com.ibosng.usercreationservice.exception.TechnicalException;
import com.ibosng.usercreationservice.service.UserCreationAnlageIbosNGService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.usercreationservice.util.Constants.FILENAME_TEMPLATE;
import static com.ibosng.usercreationservice.util.Constants.USER_CREATION_SERVICE;
import static com.ibosng.usercreationservice.util.DocumentUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCreationAnlageIbosNGServiceImpl implements UserCreationAnlageIbosNGService {

    private final SharePointProperties sharePointProperties;
    private final SharePointService sharePointService;
    private final ObjectMapper objectMapper;
    private final ManageWFsService manageWFsService;
    private final ManageWFItemsService manageWFItemsService;
    private final BenutzerService benutzerService;
    private final StammdatenService stammdatenService;
    private final VertragsdatenService vertragsdatenService;
    private final VordienstzeitenService vordienstzeitenService;
    private final GehaltInfoService gehaltInfoService;
    private final UnterhaltsberechtigteService unterhaltsberechtigteService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final ArbeitszeitenService arbeitszeitenService;
    private final ZusatzInfoService zusatzInfoService;
    private final WWorkflowGroupService wWorkflowGroupService;
    private final PersonalnummerService personalnummerService;

    @Override
    public void proccessMitarbeiters(DriveItemCollectionPage page) {
        List<DriveItem> driveItems = page.getCurrentPage();
        for (DriveItem driveItem : driveItems) {
            File mitarbeiterFile = null;
            String filename = getFilenameWithDate(driveItem.name);
            try {
                driveItem = sharePointService.moveFile(driveItem.id, filename,
                        sharePointProperties.getAngelegteBenutzerProcessing());
                mitarbeiterFile = sharePointService.downloadFile(driveItem.id, FILENAME_TEMPLATE.formatted("temp"));
                UserAnlageResponseDto userAnlageResponseDto = objectMapper.readValue(getContentFromFile(mitarbeiterFile),
                        UserAnlageResponseDto.class);
                proceedWithCorrectFile(driveItem, filename, userAnlageResponseDto);
            } catch (MSGraphServiceException | IOException | GraphServiceException | TechnicalException e) {
                log.error("Exception caught in processing MA file: ", e);
                proceedWithError(driveItem, filename);
            } finally {
                if (Objects.nonNull(mitarbeiterFile)) {
                    deleteLocalFile(mitarbeiterFile.getPath());
                }
            }
        }
    }

    private void proceedWithCorrectFile(DriveItem driveItem, String filename, UserAnlageResponseDto userAnlageResponseDto) throws TechnicalException {
        log.info("New mitarbeiter found: {}, driveItemName : {}", driveItem.name, userAnlageResponseDto);
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(userAnlageResponseDto.getPersonalnummer());
        if (personalnummer == null) {
            log.error("Could not find personalnummer for : {} , stopping process!", userAnlageResponseDto.getPersonalnummer());
            return;
        }

        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(userAnlageResponseDto.getPersonalnummer(), SWorkflows.NEUEN_MA_ANLEGEN_IM_SYSTEM);
        if (workflow == null) {
            throw new TechnicalException("No workflow found for personalnummer: " + personalnummer.getPersonalnummer());
        }
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.AD_UND_IBOS_USER_ANLEGEN, WWorkflowStatus.COMPLETED, USER_CREATION_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.IBOSNG_USER_ANLEGEN, WWorkflowStatus.IN_PROGRESS, USER_CREATION_SERVICE);
        Benutzer benutzer = createBenutzer(userAnlageResponseDto);
        if (benutzer != null) {
            manageWWGAndWWAndWWI(workflow);
            sharePointService.moveFile(driveItem.id, filename, sharePointProperties.getAngelegteBenutzerSuccessful());
            moveIbisacamResponseToSuccessful(personalnummer.getPersonalnummer());
        } else {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.IBOSNG_USER_ANLEGEN, WWorkflowStatus.ERROR, USER_CREATION_SERVICE);
            sharePointService.moveFile(driveItem.id, filename, sharePointProperties.getAngelegteBenutzerError());
            log.error("Stammdaten not found for mitarbeiter with personalnummer {}", userAnlageResponseDto.getPersonalnummer());
        }
    }

    private void moveIbisacamResponseToSuccessful(String personalnummer) {
        DriveItemCollectionPage angelegteBenutzer = sharePointService.getUploadedFiles(sharePointProperties.getNeueBenutzer());
        List<DriveItem> driveItems = angelegteBenutzer.getCurrentPage();
        for (DriveItem driveItem : driveItems) {
            if (driveItem.name != null && driveItem.name.equals(String.format(FILENAME_TEMPLATE, personalnummer))) {
                String filename = getFilenameWithDate(driveItem.name);
                sharePointService.moveFile(driveItem.id, filename,
                        sharePointProperties.getAngelegteBenutzerSuccessful());
            } else {
                log.warn("Original ibosNG request file for personalnummer {} could not be found", personalnummer);
            }
        }
    }

    private void manageWWGAndWWAndWWI(WWorkflow workflow) {
        WWorkflowGroup wWorkflowGroup = workflow.getWorkflowGroup();
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.IBOSNG_USER_ANLEGEN, WWorkflowStatus.COMPLETED, USER_CREATION_SERVICE);
        manageWFsService.setWFStatus(wWorkflowGroup, SWorkflows.NEUEN_MA_ANLEGEN_IM_SYSTEM, WWorkflowStatus.COMPLETED, USER_CREATION_SERVICE);
        List<WWorkflowItem> allWorkflowItems = manageWFItemsService.findAllByWorkflowGroup(wWorkflowGroup);
        List<WWorkflowItem> unfinishedWWI = allWorkflowItems.stream().filter(wwi -> !wwi.getStatus().equals(WWorkflowStatus.COMPLETED)).toList();
        if (unfinishedWWI.isEmpty()) {
            Personalnummer personalnummer = personalnummerService.findByPersonalnummer(workflow.getData());
            if (personalnummer == null) {
                log.error("Could not find personalnummer for : {} ", workflow.getData());
                wWorkflowGroup.setStatus(WWorkflowStatus.ERROR);
            } else {
                wWorkflowGroup.setStatus(WWorkflowStatus.COMPLETED);
                personalnummer.setOnboardedOn(getLocalDateNow());
                personalnummer = personalnummerService.save(personalnummer);
                setStatusToEntities(personalnummer);
            }
        } else {
            wWorkflowGroup.setStatus(WWorkflowStatus.ERROR);
        }
        wWorkflowGroupService.save(wWorkflowGroup);
    }

    private void setStatusToEntities(Personalnummer personalnummer) {
        personalnummer.setStatus(Status.ACTIVE);
        personalnummer = personalnummerService.save(personalnummer);
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer.getPersonalnummer());
        if (stammdaten != null) {
            stammdaten.setStatus(MitarbeiterStatus.ACTIVE);
            stammdaten = stammdatenService.save(stammdaten);
            if (stammdaten.getZusatzInfo() != null) {
                stammdaten.getZusatzInfo().setStatus(MitarbeiterStatus.ACTIVE);
                zusatzInfoService.save(stammdaten.getZusatzInfo());
            }
        }
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerString(personalnummer.getPersonalnummer()).stream().filter(vd -> vd.getStatus().equals(MitarbeiterStatus.VALIDATED)).findFirst().orElse(null);
        if (vertragsdaten != null) {
            vertragsdaten.setStatus(MitarbeiterStatus.ACTIVE);
            vertragsdaten = vertragsdatenService.save(vertragsdaten);
            GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
            if (gehaltInfo != null) {
                gehaltInfo.setStatus(MitarbeiterStatus.ACTIVE);
                gehaltInfoService.save(gehaltInfo);
            }
            List<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findAllByVertragsdatenId(vertragsdaten.getId());
            vordienstzeiten.forEach(vd -> vd.setStatus(MitarbeiterStatus.ACTIVE));
            vordienstzeitenService.saveAll(vordienstzeiten);
            List<Unterhaltsberechtigte> unterhaltsberechtigtes = unterhaltsberechtigteService.findAllByVertragsdatenId(vertragsdaten.getId());
            unterhaltsberechtigtes.forEach(vd -> vd.setStatus(MitarbeiterStatus.ACTIVE));
            unterhaltsberechtigteService.saveAll(unterhaltsberechtigtes);
        }
    }

    private void proceedWithError(DriveItem driveItem, String filename) {
        log.error("Error occurred while checking incoming files id: {} name: {}", driveItem.id, driveItem.name);
        sharePointService.moveFile(driveItem.id, filename, sharePointProperties.getAngelegteBenutzerError());
    }

    private Benutzer createBenutzer(UserAnlageResponseDto userAnlageResponseDto) {
        Benutzer benutzer = null;
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(userAnlageResponseDto.getPersonalnummer());
        if (stammdaten != null) {
            benutzer = benutzerService.findByEmail(userAnlageResponseDto.getEmail());
            if (benutzer != null) {
                log.warn("Benutzer already exists with same email {}", userAnlageResponseDto.getEmail());
                return benutzer;
            }
            benutzer = new Benutzer();
            benutzer.setStatus(Status.ACTIVE);
            benutzer.setCreatedBy(USER_CREATION_SERVICE);
            if (!isNullOrBlank(userAnlageResponseDto.getAzureId())) {
                benutzer.setAzureId(userAnlageResponseDto.getAzureId());
            } else {
                log.warn("No Azure ID was found for user {}", stammdaten.getVorname() + " " + stammdaten.getNachname());
            }
            if (!isNullOrBlank(userAnlageResponseDto.getEmail())) {
                benutzer.setEmail(userAnlageResponseDto.getEmail());
            } else {
                log.warn("No E-Mail was found for user {}", stammdaten.getVorname() + " " + stammdaten.getNachname());
            }
            benutzer.setFirstName(stammdaten.getVorname());
            benutzer.setLastName(stammdaten.getNachname());
            if (stammdaten.getPersonalnummer() != null) {
                benutzer.setPersonalnummer(stammdaten.getPersonalnummer());
            }
            benutzer = benutzerService.save(benutzer);
        }
        return benutzer;
    }

}
