package com.ibosng.lhrservice.services.impl;

import com.ibosng._service.AsyncService;
import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.IbosReference;
import com.ibosng.dbservice.entities.lhr.Abmeldung;
import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.IbosReferenceService;
import com.ibosng.dbservice.services.lhr.AbmeldungService;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DnEintritteDto;
import com.ibosng.lhrservice.dtos.DnStammStandaloneDto;
import com.ibosng.lhrservice.enums.PersoenlicheSatze;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.*;
import com.ibosng.lhrservice.utils.Helpers;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Constants.LHR_SERVICE;
import static com.ibosng.lhrservice.utils.Helpers.*;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;

@Slf4j
@Service
public class LHRDienstnehmerServiceImpl implements LHRDienstnehmerService {


    private final LHRClient lhrClient;

    private final LHRMapperService lhrMapperService;
    private final IbisFirmaService ibisFirmaService;
    private final PersonalnummerService personalnummerService;
    private final VertragsdatenService vertragsdatenService;
    private final AbmeldungService abmeldungService;
    private final LHREnvironmentService lhrEnvironmentService;
    private final VariableDatenService variableDatenService;
    private final PersoenlicheSaetzeService persoenlicheSaetzeService;
    private final KostenstellenaufteilungService kostenstellenaufteilungService;
    private final EintrittService eintrittService;
    private final DienstraederService dienstraederService;
    private final SondertageService sondertageService;
    private final MitversicherteService mitversicherteService;
    private final WWorkflowItemService wWorkflowItemService;
    private final ManageWFItemsService manageWFItemsService;
    private final IbosReferenceService ibosReferenceService;
    private final MailService mailService;
    private final AzureSSOService azureSSOService;
    private final BenutzerService benutzerService;
    private final AsyncService asyncService;

    public LHRDienstnehmerServiceImpl(LHRClient lhrClient,
                                      LHRMapperService lhrMapperService,
                                      IbisFirmaService ibisFirmaService,
                                      PersonalnummerService personalnummerService,
                                      VertragsdatenService vertragsdatenService,
                                      AbmeldungService abmeldungService,
                                      LHREnvironmentService lhrEnvironmentService,
                                      VariableDatenService variableDatenService,
                                      PersoenlicheSaetzeService persoenlicheSaetzeService,
                                      KostenstellenaufteilungService kostenstellenaufteilungService,
                                      EintrittService eintrittService,
                                      DienstraederService dienstraederService,
                                      SondertageService sondertageService,
                                      MitversicherteService mitversicherteService,
                                      WWorkflowItemService wWorkflowItemService,
                                      ManageWFItemsService manageWFItemsService,
                                      IbosReferenceService ibosReferenceService,
                                      MailService mailService,
                                      AzureSSOService azureSSOService,
                                      BenutzerService benutzerService,
                                      AsyncService asyncService) {
        this.lhrClient = lhrClient;
        this.lhrMapperService = lhrMapperService;
        this.ibisFirmaService = ibisFirmaService;
        this.personalnummerService = personalnummerService;
        this.vertragsdatenService = vertragsdatenService;
        this.abmeldungService = abmeldungService;
        this.lhrEnvironmentService = lhrEnvironmentService;
        this.variableDatenService = variableDatenService;
        this.persoenlicheSaetzeService = persoenlicheSaetzeService;
        this.kostenstellenaufteilungService = kostenstellenaufteilungService;
        this.eintrittService = eintrittService;
        this.dienstraederService = dienstraederService;
        this.sondertageService = sondertageService;
        this.mitversicherteService = mitversicherteService;
        this.wWorkflowItemService = wWorkflowItemService;
        this.manageWFItemsService = manageWFItemsService;
        this.ibosReferenceService = ibosReferenceService;
        this.mailService = mailService;
        this.azureSSOService = azureSSOService;
        this.benutzerService = benutzerService;
        this.asyncService = asyncService;
    }

    @Override
    public ResponseEntity<DnStammStandaloneDto> getDienstnehmerstamm(Integer personalnummerId) {
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (pn == null) {
            log.error("Personalnummer object not found for id {} in getDienstnehmerstamm", personalnummerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            if (pn.getFirma() != null) {
                return lhrClient.getDienstnehmerstammFromLHR(lhrEnvironmentService.getFaKz(pn.getFirma()), lhrEnvironmentService.getFaNr(pn.getFirma()), parseStringToInteger(pn.getPersonalnummer()));
            } else {
                log.error("IbisFirma is null for personalnummer {}", pn.getPersonalnummer());
                return null;
            }

        } catch (LHRWebClientException e) {
            handleLHRWebClientException(e, pn.getPersonalnummer(), "dienstnehmerstamm");
        } catch (Exception e) {
            log.error("An unexpected error occurred while calling LHR client for getting dienstnehmerstamm for personalnummer: {} with message: {}", pn.getPersonalnummer(), e.getMessage());
        }
        return null;
    }

    @Override
    public ResponseEntity<List<DnStammStandaloneDto>> findAllDienstnehmers(String firma, Integer minDnNr, Integer maxDnNr,
                                                                           String effectiveDate, String activeSince) {
        IbisFirma ibisFirma = ibisFirmaService.findByName(firma);
        if (ibisFirma == null) {
            log.error("Ibis firma not found for name {}", firma);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            return lhrClient.findAllDienstnehmersFromLHR(ibisFirma.getLhrKz(), ibisFirma.getLhrNr(), minDnNr, maxDnNr, effectiveDate, activeSince);
        } catch (LHRWebClientException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage())).build();
        }
    }

    @Override
    public ResponseEntity<?> createOrUpdateLHRDienstnehmerstamm(Integer personalnummerId) throws LHRException {
        ResponseEntity<DnStammStandaloneDto> lhrEntityResponse = null;
        String nameMA = "";
        Personalnummer pn = personalnummerService.findById(personalnummerId).orElse(null);
        if (pn == null) {
            log.error("Personalnummer object not found for id {} in getDienstnehmerstamm", personalnummerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            DnStammStandaloneDto dnStammStandalone = lhrMapperService.mapStammdatenAndVertragsdaten(pn);
            //First check if there is data in LHR
            ResponseEntity<DnStammStandaloneDto> existingDnStammStandaloneDtoResponse = getDienstnehmerstamm(personalnummerId);
            DnStammStandaloneDto existingDnStammStandalone = getBodyIfStatusOk(existingDnStammStandaloneDtoResponse);

            DnStammStandaloneDto entityToBeSaved;

            if (existingDnStammStandalone != null) {
                //If data exist in LHR, update the data, send it to LHR via PUT and save the response in ibosNG
                entityToBeSaved = lhrMapperService.updateExistingDnStammStandalone(dnStammStandalone, existingDnStammStandalone);
                lhrEntityResponse = lhrClient.updateDienstnehmerstammOnLHR(entityToBeSaved);
            } else {
                //If data does not exist in LHR, send it the data via POST and save the response in ibosNG
                entityToBeSaved = dnStammStandalone;
                lhrEntityResponse = lhrClient.sendDienstnehmerstammToLHR(dnStammStandalone);
            }
            nameMA = entityToBeSaved.getDienstnehmerstamm().getName();

            manageRestData(pn);
            if (lhrEntityResponse.getStatusCode().equals(HttpStatus.OK)) {
                return ResponseEntity.ok().build();
            }
            log.error("Response from LHR server was null for personalnummer {}", pn.getPersonalnummer());
        } catch (LHRWebClientException ex) {
            handleLHRWebClientException(ex, pn.getPersonalnummer(), "VariableDaten");
        } catch (Exception e) {
            String[] mailITRecipients = getEmailRecipients(IbosRole.IT);
            mailService.sendEmail("lhr-service.ma-mitarbeiterdaten-lesen-und-bearbeiten-fehlgeschlagen",
                    "german",
                    null,
                    mailITRecipients,
                    toObjectArray(nameMA),
                    toObjectArray(nameMA, Helpers.getDateAndTimeInEmailFormat(LocalDateTime.now()), e.getMessage()));

            log.error("An unexpected error occurred while calling LHR client for saving dienstnehmerstamm for personalnummer: {} with message: {}", pn.getPersonalnummer(), e.getMessage());
            throw new LHRException(String.format("An unexpected error occurred while calling LHR client for saving dienstnehmerstamm for personalnummer: %s with message: %s", pn.getPersonalnummer(), e.getMessage()));
        }
        if (lhrEntityResponse != null) {
            return ResponseEntity.status(lhrEntityResponse.getStatusCode()).body(lhrEntityResponse.getBody());
        }
        throw new LHRException(String.format("An unexpected error occurred while calling LHR client for saving dienstnehmerstamm for personalnummer: %s", pn.getPersonalnummer()));
    }

    private String[] getEmailRecipients(IbosRole role) {
        String[] mailITRecipients = new String[0];
        try {
            mailITRecipients = azureSSOService.getGroupMemberEmailsByName(role.getValue()).toArray(new String[0]);
        } catch (Exception e) {
            log.error("No recipients found for role {}", role.getValue());
        }
        return mailITRecipients;
    }

    private void manageRestData(Personalnummer pn) throws LHRException {
        if (pn == null) {
            log.error("Personalnummer object not found in manageRestData");
            return;
        }
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(pn.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null || vertragsdaten.getEintritt() == null) {
            log.error("Vertragsdaten object not found for personalnummer {}", pn.getPersonalnummer());
            return;
        }
        String eintrittsdatum = getDateInPostRequestFormat(vertragsdaten.getEintritt());


        asyncService.asyncExecutorVoid(() -> variableDatenService.processVariableDaten(pn, eintrittsdatum));
        asyncService.asyncExecutorVoid(() -> eintrittService.processEintritt(pn, eintrittsdatum));
        asyncService.asyncExecutorVoid(() -> kostenstellenaufteilungService.processKostenstellenaufteilung(pn, eintrittsdatum));
        asyncService.asyncExecutorVoid(() -> persoenlicheSaetzeService.processPersoenlicheSaetze(pn, vertragsdaten.getEintritt(), PersoenlicheSatze.WOCHENSTUNDEN));
        asyncService.asyncExecutorVoid(() -> persoenlicheSaetzeService.processPersoenlicheSaetze(pn, vertragsdaten.getEintritt(), PersoenlicheSatze.ARBEITSTAGE));
        asyncService.asyncExecutorVoid(() -> mitversicherteService.processMitversicherte(pn, vertragsdaten.getId()));
        asyncService.asyncExecutorVoid(() -> dienstraederService.processDienstraeder(pn, vertragsdaten.getEintritt()));
        asyncService.asyncExecutorVoid(() -> sondertageService.processSondertageForOnboarding(pn, vertragsdaten.getId()));

        if (persoenlicheSaetzeService.shouldSendGehalt(vertragsdaten.getId())) {
            asyncService.asyncExecutorVoid(() -> persoenlicheSaetzeService.processPersoenlicheSaetze(pn, vertragsdaten.getEintritt(), PersoenlicheSatze.GEHALT));
        }
    }

    /**
     * Processes Abmeldung of a Teilnehmer
     * <ul>
     *     <li> Submit entry to LHR</li>
     *     <li> Update Abmeldung status based on LHR Response </li>
     *     <li> Save to Database into table Abmeldung</li>
     *     <li> Send mail to LV that TN was abgemeldet </li>
     * </ul>
     *
     * @param abmeldungDto
     * @return ResponseEntity containing the response from the LHR service or LHRWebclientException
     */
    @Override
    public ResponseEntity<?> mapAndSendUebaAbmeldung(AbmeldungDto abmeldungDto) {
        Optional<Abmeldung> abmeldungOptional = abmeldungService.findById(abmeldungDto.getId());
        if (abmeldungOptional.isEmpty()) {
            log.error("No abmeldung could be found for id {}", abmeldungDto.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Abmeldung abmeldung = abmeldungOptional.get();
        abmeldung.setStatus(AbmeldungStatus.ABMELDUNG_BEI_LV);
        abmeldung = abmeldungService.save(abmeldung);

        if (abmeldung.getWorkflow() == null) {
            log.error("No workflow could be found for abmeldung {}", abmeldungDto.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        WWorkflow workflow = abmeldung.getWorkflow();
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_TRITT_AUS_SEND_AUSTRITT_TO_LHR, WWorkflowStatus.IN_PROGRESS, LHR_SERVICE);
        DnEintritteDto dn = lhrMapperService.createEintritteForAbmeldung(abmeldungDto);

        if (dn == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String personalnummer = null;
        if (abmeldung.getPersonalnummer() != null) {
            personalnummer = abmeldung.getPersonalnummer().getPersonalnummer();
        } else {
            List<IbosReference> ibosReferenceList = ibosReferenceService.findAllByData(abmeldungDto.getSvNummer().toString());
            IbosReference ibosReference = findFirstObject(ibosReferenceList, new HashSet<>(List.of(abmeldungDto.getSvNummer().toString())), "IbosReference");
            if (ibosReference != null && ibosReference.getIbosId() != null) {
                personalnummer = ibosReference.getIbosId().toString();
            }
        }
        if (isNullOrBlank(personalnummer)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ResponseEntity<DnEintritteDto> response;
        try {
            response = lhrClient.postEintritt(
                    dn.getDienstnehmer().getFaKz(),
                    dn.getDienstnehmer().getFaNr(),
                    parseStringToInteger(personalnummer),
                    null,
                    null,
                    dn
            );
        } catch (LHRWebClientException ex) {
            handlePostEintrittError(ex.getMessage(), workflow, abmeldung, SWorkflowItems.TN_TRITT_AUS_SEND_AUSTRITT_TO_LHR);
            return handleLHRWebClientException(ex, personalnummer, "UEBAAustritt");
        }

        if (response != null && !response.getStatusCode().is2xxSuccessful()) {
            log.error("LHR returned a non-successful response when calling POST eintritte: {}", response.getStatusCode());
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_TRITT_AUS_SEND_AUSTRITT_TO_LHR, WWorkflowStatus.ERROR, LHR_SERVICE);
            updateAbmeldungStatus(abmeldung, AbmeldungStatus.ERROR);
            return response;
        }
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_TRITT_AUS_SEND_AUSTRITT_TO_LHR, WWorkflowStatus.COMPLETED, LHR_SERVICE);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_TRITT_AUS_CHANGE_STATUS, WWorkflowStatus.IN_PROGRESS, LHR_SERVICE);
        abmeldung.setStatus(AbmeldungStatus.ABGEMELDET);
        abmeldungService.save(abmeldung);
        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_TRITT_AUS_CHANGE_STATUS, WWorkflowStatus.COMPLETED, LHR_SERVICE);

        Benutzer benutzer = benutzerService.findByPersonalnummerAndFirmaBmdClient(abmeldung.getPersonalnummer().getPersonalnummer(),
                abmeldung.getPersonalnummer().getFirma().getBmdClient());

        String nameTN = String.join(" ", benutzer.getFirstName(), benutzer.getLastName());
        String[] mailLohnverrechnungRecipients = azureSSOService.getGroupMemberEmailsByName(IbosRole.LV.getValue()).toArray(new String[0]);
        mailService.sendEmail("gateway-service.tn-austritt-transfer-success",
                "german",
                null,
                mailLohnverrechnungRecipients,
                toObjectArray(),
                toObjectArray(nameTN, abmeldung.getAustrittsDatum()));
        log.info("Saved ueba abmeldung to database for personalnummer: {}", personalnummer);

        return response;
    }

    private void handlePostEintrittError(String errorMessage, WWorkflow workflow, Abmeldung abmeldung, SWorkflowItems sWorkflowItem) {
        log.error("LHR returned an error: {}", errorMessage);
        WWorkflowItem wWorkflowItem = manageWFItemsService.setWFItemStatus(workflow, sWorkflowItem, WWorkflowStatus.ERROR, LHR_SERVICE);
        wWorkflowItem.setData(errorMessage);
        wWorkflowItemService.save(wWorkflowItem);
        updateAbmeldungStatus(abmeldung, AbmeldungStatus.ERROR);
    }

    private void updateAbmeldungStatus(Abmeldung abmeldung, AbmeldungStatus status) {
        abmeldung.setStatus(status);
        abmeldungService.save(abmeldung);
    }
}