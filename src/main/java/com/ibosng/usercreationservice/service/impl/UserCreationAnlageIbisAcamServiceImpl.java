package com.ibosng.usercreationservice.service.impl;

import com.ibosng.dbservice.entities.ZusatzInfo;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.ZusatzInfoService;
import com.ibosng.dbservice.services.mitarbeiter.*;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.ibosng.usercreationservice.dto.UserAnlageDto;
import com.ibosng.usercreationservice.exception.TechnicalException;
import com.ibosng.usercreationservice.service.UserCreationAnlageIbisAcamService;
import com.ibosng.usercreationservice.service.UserCreationMitarbeiterMapperService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;
import static com.ibosng.usercreationservice.util.Constants.FILENAME_TEMPLATE;
import static com.ibosng.usercreationservice.util.Constants.USER_CREATION_SERVICE;
import static com.ibosng.usercreationservice.util.DocumentUtils.deleteLocalFile;
import static com.ibosng.usercreationservice.util.DocumentUtils.getPrefixFromFilename;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCreationAnlageIbisAcamServiceImpl implements UserCreationAnlageIbisAcamService {

    private final SharePointProperties sharePointProperties;
    private final VertragsdatenService vertragsdatenService;
    private final StammdatenService stammdatenService;
    private final SharePointService sharePointService;
    private final ManageWFsService manageWFsService;
    private final ManageWFItemsService manageWFItemsService;
    private final UserCreationMitarbeiterMapperService userCreationMitarbeiterMapperService;
    private final PersonalnummerService personalnummerService;
    private final ZusatzInfoService zusatzInfoService;
    private final BankDatenService bankDatenService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final GehaltInfoService gehaltInfoService;
    private final MailService mailService;
    private final AzureSSOService azureSSOService;

    @Override
    public boolean createMitarbeiterFile(String personalnummer) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.NEUEN_MA_ANLEGEN_IM_SYSTEM);
        if (workflow == null) {
            log.error("Could not find workflow for personalnummer: {} , stopping process!", personalnummer);
            return false;
        }
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(personalnummer);
        if (personalnummerEntity == null) {
            log.error("Could not find personalnummer for : {} , stopping process!", personalnummer);
            return false;
        }
        manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.NEUEN_MA_ANLEGEN_IM_SYSTEM, WWorkflowStatus.IN_PROGRESS, USER_CREATION_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.NEUEN_MA_ANLEGEN, WWorkflowStatus.IN_PROGRESS, USER_CREATION_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.NEUEN_MA_ANLEGEN, WWorkflowStatus.COMPLETED, USER_CREATION_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.USER_ANLAGE_BEAUFTRAGEN, WWorkflowStatus.IN_PROGRESS, USER_CREATION_SERVICE);


        final Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
        String nameMA = String.join(" ", stammdaten.getVorname(), stammdaten.getNachname());
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerStringAndStatus(personalnummer, List.of(MitarbeiterStatus.VALIDATED)).stream().findFirst().orElse(null);
        setStammdatenActive(stammdaten);
        setVertragsdatenActive(vertragsdaten);
        UserAnlageDto userAnlageDto;
        try {
            userAnlageDto = userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten);
        } catch (TechnicalException ex) {
            log.error("Caught exception while mapping to dto for personalnummer: {}, stopping process! Exception: ", personalnummer, ex);
            return false;
        }

        File mitarbeiterTempFile;
        try {
            mitarbeiterTempFile = sharePointService.createJsonFileFromDto(userAnlageDto,
                    getPrefixFromFilename(FILENAME_TEMPLATE.formatted(personalnummer)));
        } catch (MSGraphServiceException ex) {
            log.error("Error creating temporary file ", ex);
            return false;
        }

        try {
            sharePointService.uploadFile(mitarbeiterTempFile.getPath(), FILENAME_TEMPLATE.formatted(
                    userAnlageDto.getPersonalnummer()), sharePointProperties.getNeueBenutzer());
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.USER_ANLAGE_BEAUFTRAGEN, WWorkflowStatus.COMPLETED, USER_CREATION_SERVICE);
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.AD_UND_IBOS_USER_ANLEGEN, WWorkflowStatus.IN_PROGRESS, USER_CREATION_SERVICE);
            return true;
        } catch (MSGraphServiceException ex) {
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.USER_ANLAGE_BEAUFTRAGEN, WWorkflowStatus.ERROR, USER_CREATION_SERVICE);
            mailService.sendEmail("gateway-service.ma-beauftragung-fehlgeschlagen",
                    "german",
                    null,
                    ArrayUtils.addAll(azureSSOService
                            .getGroupMemberEmailsByName(IbosRole.HR.getValue())
                            .toArray(new String[0]), azureSSOService
                            .getGroupMemberEmailsByName(IbosRole.IT.getValue())
                            .toArray(new String[0])),
                    toObjectArray(nameMA),
                    toObjectArray(nameMA));

            return false;
        } finally {
            deleteLocalFile(mitarbeiterTempFile.getPath());
        }
    }

    public boolean setStammdatenActive(Stammdaten stammdaten) {
        //Stammdaten  part\
        if (stammdaten != null) {

            ZusatzInfo zusatzInfo = stammdaten.getZusatzInfo();
            BankDaten bankDaten = stammdaten.getBank();

            final boolean isStammdatenActive = MitarbeiterStatus.VALIDATED.equals(zusatzInfo.getStatus()) && MitarbeiterStatus.VALIDATED.equals(bankDaten.getStatus()) && MitarbeiterStatus.VALIDATED.equals(stammdaten.getStatus());
            zusatzInfo.setStatus(isStammdatenActive ? MitarbeiterStatus.ACTIVE : MitarbeiterStatus.INACTIVE);
            zusatzInfo.setChangedOn(getLocalDateNow());
            zusatzInfo.setChangedBy(USER_CREATION_SERVICE);
            stammdaten.setZusatzInfo(zusatzInfoService.save(zusatzInfo));

            bankDaten.setStatus(isStammdatenActive ? MitarbeiterStatus.ACTIVE : MitarbeiterStatus.INACTIVE);
            bankDaten.setChangedOn(getLocalDateNow());
            bankDaten.setChangedBy(USER_CREATION_SERVICE);
            stammdaten.setBank(bankDatenService.save(bankDaten));

            stammdaten.setStatus(isStammdatenActive ? MitarbeiterStatus.ACTIVE : MitarbeiterStatus.INACTIVE);
            stammdaten.setChangedOn(getLocalDateNow());
            stammdaten.setChangedBy(USER_CREATION_SERVICE);
            stammdatenService.save(stammdaten);

            log.info("Stammdaten status changed, is ACTIVE - {}", isStammdatenActive);
            return isStammdatenActive;
        }

        log.warn("Stammdaten null, can not change status");
        return false;
    }

    public boolean setVertragsdatenActive(Vertragsdaten vertragsdaten) {
        //Vertragsdaten part
        if (vertragsdaten != null) {
            ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
            GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
            final boolean isVertragsdatenActive = MitarbeiterStatus.VALIDATED.equals(vertragsdaten.getStatus()) &&
                    gehaltInfo != null && MitarbeiterStatus.VALIDATED.equals(gehaltInfo.getStatus()) && arbeitszeitenInfo != null &&
                    MitarbeiterStatus.VALIDATED.equals(arbeitszeitenInfo.getStatus());

            vertragsdaten.setStatus(isVertragsdatenActive ? MitarbeiterStatus.ACTIVE : MitarbeiterStatus.INACTIVE);
            vertragsdaten.setChangedOn(getLocalDateNow());
            vertragsdaten.setChangedBy(USER_CREATION_SERVICE);
            vertragsdatenService.save(vertragsdaten);

            if (gehaltInfo != null) {
                gehaltInfo.setStatus(isVertragsdatenActive ? MitarbeiterStatus.ACTIVE : MitarbeiterStatus.INACTIVE);
                gehaltInfo.setChangedOn(getLocalDateNow());
                gehaltInfo.setChangedBy(USER_CREATION_SERVICE);
                gehaltInfoService.save(gehaltInfo);
            }

            if (arbeitszeitenInfo != null) {
                arbeitszeitenInfo.setChangedOn(getLocalDateNow());
                arbeitszeitenInfo.setChangedBy(USER_CREATION_SERVICE);
                arbeitszeitenInfo.setStatus(isVertragsdatenActive ? MitarbeiterStatus.ACTIVE : MitarbeiterStatus.INACTIVE);
                arbeitszeitenInfoService.save(arbeitszeitenInfo);
            }
            log.info("Vertragsdaten status changed, is ACTIVE - {}", isVertragsdatenActive);
            return isVertragsdatenActive;
        }

        log.warn("Vertragsdaten null, can not change status");
        return false;
    }
}

