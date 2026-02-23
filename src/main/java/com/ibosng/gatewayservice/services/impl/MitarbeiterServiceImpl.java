package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.Projekt2ManagerDto;
import com.ibosng.dbservice.dtos.Seminar2TrainerDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.moxis.MoxisJobDto;
import com.ibosng.dbservice.dtos.workflows.WorkflowDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.moxis.SigningJobType;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.Projekt2ManagerService;
import com.ibosng.dbservice.services.Seminar2TrainerService;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.UnterhaltsberechtigteService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VordienstzeitenService;
import com.ibosng.dbservice.services.workflows.WWorkflowService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.GenericMAService;
import com.ibosng.gatewayservice.services.MAOnboardingService;
import com.ibosng.gatewayservice.services.MAVerwaltenService;
import com.ibosng.gatewayservice.services.MitarbeiterService;
import com.ibosng.gatewayservice.services.WorkflowHelperService;
import com.ibosng.moxisservice.services.MoxisJobRestService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ibosng.gatewayservice.utils.Constants.FN_STAMMDATEN_ERFASSEN;
import static com.ibosng.gatewayservice.utils.Constants.FN_VERTRAGSDATEN_ERFASSEN;
import static com.ibosng.gatewayservice.utils.Constants.GATEWAY_SERVICE;
import static com.ibosng.gatewayservice.utils.Helpers.checkResultIfNull;
import static com.ibosng.gatewayservice.utils.Helpers.getTokenFromAuthorizationHeader;

@Service
@Slf4j
@Qualifier("mitarbeiterServiceImpl")
@RequiredArgsConstructor
public class MitarbeiterServiceImpl implements MitarbeiterService {
    private final MAOnboardingService maOnboardingService;
    private final MAVerwaltenService maVerwaltenService;

    @PersistenceContext
    private EntityManager entityManager;

    private final StammdatenService stammdatenService;
    private final PersonalnummerService personalnummerService;
    private final BenutzerDetailsService benutzerDetailsService;
    private final ManageWFsService manageWFsService;
    private final WorkflowHelperService workflowHelperService;
    private final VertragsdatenService vertragsdatenService;
    private final ManageWFItemsService manageWFItemsService;
    private final VordienstzeitenService vordienstzeitenService;
    private final UnterhaltsberechtigteService unterhaltsberechtigteService;
    private final WWorkflowService wWorkflowService;
    private final Seminar2TrainerService seminar2TrainerService;
    private final Projekt2ManagerService projekt2ManagerService;
    private final VereinbarungService vereinbarungService;
    private final BenutzerService benutzerService;
    private final GenericMAService genericMAService;
    private final MoxisJobRestService moxisJobRestService;

    @Override
    public ResponseEntity<PayloadResponse> getStammdaten(String personalnummer, Boolean isOnboarding, String authorizationHeader) {
        if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(personalnummer, authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadTypeList<StammdatenDto> stammDatenPayloadType;
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(personalnummer);
        Stammdaten stammdaten = getStammdatenEntity(personalnummer, isOnboarding);
        if (stammdaten != null) {
            stammDatenPayloadType = genericMAService.createPayloadForStammdaten(stammdatenService.mapStammdatenToDto(stammdaten));
        } else {
            StammdatenDto stammdatenDto = new StammdatenDto();
            stammdatenDto.setPersonalnummer(personalnummer);
            stammdatenDto.setFirma(personalnummerEntity.getFirma().getName());
            stammDatenPayloadType = genericMAService.createPayloadForStammdaten(stammdatenDto);
        }

        PayloadResponse response = new PayloadResponse();
        if (personalnummerEntity == null) {
            response.setSuccess(false);
            return checkResultIfNull(response);
        }
        WWorkflow workflow = genericMAService.getWorkflowForMaType(personalnummerEntity);

        PayloadTypeList<MitarbeiterType> createPayloadForMAType = genericMAService.createPayloadForMAType(personalnummerEntity.getMitarbeiterType());
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);

        response.setSuccess(true);
        response.setData(Arrays.asList(createPayloadForMAType, stammDatenPayloadType, workflowDtoPayloadType));
        return checkResultIfNull(response);
    }

    protected Stammdaten getStammdatenEntity(String personalnummer, Boolean isOnboarding) {
        Stammdaten stammdaten;
        if (isOnboarding) {
            stammdaten = stammdatenService.findByPersonalnummerStringAndStatusIn(personalnummer, List.of(MitarbeiterStatus.NEW, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED));
        } else {
            stammdaten = stammdatenService.findByPersonalnummerStringAndStatusIn(personalnummer, List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED));
        }
        return stammdaten;
    }

    @Override
    public ResponseEntity<PayloadResponse> saveStammdaten(StammdatenDto stammdatenDto, Integer workflowId, Boolean isOnboarding, String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        PayloadResponse response;
        if (isOnboarding) {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(stammdatenDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maOnboardingService.saveStammdaten(stammdatenDto, workflowId, token);
        } else {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(stammdatenDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maVerwaltenService.saveStammdaten(stammdatenDto, token);
        }
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }



    @Override
    public ResponseEntity<PayloadResponse> getVertragsdaten(String personalnummer, Boolean isOnboarding, String authorizationHeader) {
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(personalnummer);
        PayloadResponse response = new PayloadResponse();
        if (personalnummerEntity == null) {
            response.setSuccess(false);
            return checkResultIfNull(response);
        }
        boolean isMitarbeiter = personalnummerEntity.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
        Vertragsdaten vertragsdaten = genericMAService.getVertragsdatenEntity(personalnummer, isOnboarding);
        PayloadTypeList<VertragsdatenDto> vertragsDatenDtoPayloadType;
        if (vertragsdaten != null) {
            vertragsDatenDtoPayloadType = genericMAService.createPayloadForVertragsdaten(vertragsdatenService.mapVertragsdatenToDto(vertragsdaten), isMitarbeiter);
        } else {
            VertragsdatenDto vertragsdatenDto = new VertragsdatenDto();
            vertragsdatenDto.setPersonalnummer(personalnummer);
            vertragsDatenDtoPayloadType = genericMAService.createPayloadForVertragsdaten(vertragsdatenDto, isMitarbeiter);
        }

        WWorkflow workflow = genericMAService.getWorkflowForMaType(personalnummerEntity);

        PayloadTypeList<MitarbeiterType> createPayloadForMAType = genericMAService.createPayloadForMAType(personalnummerEntity.getMitarbeiterType());
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(createPayloadForMAType, vertragsDatenDtoPayloadType, workflowDtoPayloadType));
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> saveVertragsdaten(VertragsdatenDto vertragsDatenDto, Integer workflowId, Boolean isOnboarding, String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        PayloadResponse response;
        if (isOnboarding) {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(vertragsDatenDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maOnboardingService.saveVertragsdaten(vertragsDatenDto, workflowId, token);
        } else {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(vertragsDatenDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maVerwaltenService.saveVertragsdaten(vertragsDatenDto, token);
        }
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);

    }

    @Override
    public ResponseEntity<PayloadResponse> getVordienstzeiten(String personalnummer, Boolean isOnboarding, String authorizationHeader) {
        PayloadTypeList<VordienstzeitenDto> vordienstzeitenDtoPayloadType = genericMAService.createPayloadForMultipleVordienstzeiten(personalnummer, isOnboarding);
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        if (isOnboarding) {
            WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
            response.setData(Arrays.asList(vordienstzeitenDtoPayloadType, workflowDtoPayloadType));
        } else {
            response.setData(Collections.singletonList(vordienstzeitenDtoPayloadType));
        }
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> saveVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, Boolean isOnboarding, String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        PayloadResponse response;
        if (isOnboarding) {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(vordienstzeitenDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maOnboardingService.saveVordienstzeiten(vordienstzeitenDto, token);
        } else {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(vordienstzeitenDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maVerwaltenService.saveVordienstzeiten(vordienstzeitenDto, token);
        }
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteVordienstzeiten(Integer vordienstzeitenId, String authorizationHeader) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Arrays.asList(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = new PayloadResponse();
        vordienstzeitenService.deleteById(vordienstzeitenId);
        response.setSuccess(true);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> getUnterhaltsberechtigte(String personalnummer, Boolean isOnboarding, String authorizationHeader) {
        PayloadTypeList<UnterhaltsberechtigteDto> unterhaltsberechtigteDtoPayloadType = genericMAService.createPayloadForMultipleUnterhaltsberechtigte(personalnummer, isOnboarding);
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        if (isOnboarding) {
            WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
            PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
            response.setData(Arrays.asList(unterhaltsberechtigteDtoPayloadType, workflowDtoPayloadType));
        } else {
            response.setData(Collections.singletonList(unterhaltsberechtigteDtoPayloadType));
        }
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> saveUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, Boolean isOnboarding, String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        PayloadResponse response;
        if (isOnboarding) {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(unterhaltsberechtigteDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maOnboardingService.saveUnterhaltsberechtigte(unterhaltsberechtigteDto, token); //todo
        } else {
            if (!benutzerDetailsService.checkIfUserIsEligibleForMAOrTN(unterhaltsberechtigteDto.getPersonalnummer(), authorizationHeader)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            response = maVerwaltenService.saveUnterhaltsberechtigte(unterhaltsberechtigteDto, token); //todo
        }
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> deleteUnterhaltsberechtigte(Integer unterhaltsberechtigteId, String authorizationHeader) {
        if (!benutzerDetailsService.isUserEligible(getTokenFromAuthorizationHeader(authorizationHeader), Arrays.asList(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        PayloadResponse response = new PayloadResponse();
        unterhaltsberechtigteService.deleteById(unterhaltsberechtigteId);
        response.setSuccess(true);
        if (!response.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(response);
    }

    @Override
    public ResponseEntity<PayloadResponse> sendMoxisSigningRequest(String personalnummer, String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Arrays.asList(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Benutzer changedBy = benutzerDetailsService.getUserFromToken(token);
        MoxisJobDto moxisJobDto = new MoxisJobDto();
        WWorkflow contractWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG);
        WWorkflow zusatzWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_UNTERSCHRIFTENLAUF);
        if (zusatzWorkflow != null) {
            moxisJobDto.setWorkflow(contractWorkflow.getId());
            moxisJobDto.setSigningJobType(SigningJobType.ZUSATZ);
        } else if (contractWorkflow != null) {
            moxisJobDto.setWorkflow(contractWorkflow.getId());
            moxisJobDto.setSigningJobType(SigningJobType.CONTRACT);
        }
        moxisJobDto.setPersonalNummer(personalnummer);
        moxisJobDto.setChangedBy(changedBy.getEmail());
        moxisJobDto.setDescription(String.format("New contract for personalnummer: %s", personalnummer));
        log.info("Sending request for start process to moxis-service for personalnummer {}", personalnummer);
        ResponseEntity<String> responseMoxis = moxisJobRestService.startProcess(moxisJobDto, null, true);
        log.info("Received response for start process from moxis-service for personalnummer {}, with status {}, body {}", personalnummer, responseMoxis.getStatusCode(), responseMoxis.getBody());
        PayloadResponse payloadResponse = createPayloadResponseForMoxis(responseMoxis.getBody(), (zusatzWorkflow == null) ? contractWorkflow : zusatzWorkflow);
        if (!payloadResponse.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(payloadResponse);
    }

    @Override
    public ResponseEntity<PayloadResponse> sendVereinbarungSigningRequest(String authorizationHeader, Integer vereinbarungId) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Benutzer changedBy = benutzerDetailsService.getUserFromToken(token);
        MoxisJobDto moxisJobDto = new MoxisJobDto();

        Optional<Vereinbarung> vereinbarung = vereinbarungService.findById(vereinbarungId);
        if(vereinbarung.isEmpty()){
            log.error("No Vereinbarung found for Id: " + vereinbarungId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        manageWFsService.setWFStatus(vereinbarung.get().getWorkflow().getWorkflowGroup(), SWorkflows.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF, WWorkflowStatus.IN_PROGRESS, changedBy.getEmail());
        manageWFItemsService.setWFItemStatus(vereinbarung.get().getWorkflow(), SWorkflowItems.MA_FK_VEREINBARUNG_UNTERSCHRIFTENLAUF_STARTEN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);

        WWorkflow vereinbarungWorkflow = vereinbarung.get().getWorkflow();
        if (vereinbarungWorkflow != null) {
            moxisJobDto.setWorkflow(vereinbarungWorkflow.getId());
            moxisJobDto.setSigningJobType(SigningJobType.VEREINBARUNG);
        }
        // Pass VereinbarungName as description
        moxisJobDto.setDescription( vereinbarung.get().getVereinbarungName());
        moxisJobDto.setPersonalNummer(vereinbarung.get().getPersonalnummer().getPersonalnummer());
        moxisJobDto.setChangedBy(changedBy.getEmail());
        moxisJobDto.setSigningJobType(SigningJobType.VEREINBARUNG);
        log.info("Sending request for start process to moxis-service for personalnummer {}", vereinbarung.get().getPersonalnummer().getPersonalnummer());
        ResponseEntity<String> responseMoxis = moxisJobRestService.startProcess(moxisJobDto, null, true);
        WWorkflow updatedVereinbarungWorkflow = wWorkflowService.findFreshWorkflowById(vereinbarungWorkflow.getId());
        log.info("Received response for start process from moxis-service for personalnummer {}, with status {}, body {}", vereinbarung.get().getPersonalnummer().getPersonalnummer(), responseMoxis.getStatusCode(), responseMoxis.getBody());
        return checkResultIfNull(createPayloadResponseForMoxis(responseMoxis.getBody(), updatedVereinbarungWorkflow));
    }

    @Override
    public PayloadResponse getSeminars(String personalnummer) {
        Benutzer benutzer = benutzerService.getBenutzerByPersonalnummer(personalnummer);
        if (benutzer == null) {
            return PayloadResponse.builder()
                    .message("Benutzer with personalnummer %s not found".formatted(personalnummer))
                    .success(false)
                    .build();
        }
        List<Seminar2TrainerDto> seminar2TrainerDtoList = seminar2TrainerService.findAllByTrainerId(benutzer.getId()).stream().map(seminar2TrainerService::map).distinct().toList();
        PayloadTypeList<Seminar2TrainerDto> payloadTypeList = new PayloadTypeList<>(PayloadTypes.TRAINER_SEMINAR.getValue(), seminar2TrainerDtoList);
        return PayloadResponse.builder().success(true).data(List.of(payloadTypeList)).build();
    }

    @Override
    public PayloadResponse getProjekts(String personalnummer) {
        Benutzer benutzer = benutzerService.getBenutzerByPersonalnummer(personalnummer);
        if (benutzer == null) {
            return PayloadResponse.builder()
                    .message("Benutzer with personalnummer %s not found".formatted(personalnummer))
                    .success(false)
                    .build();
        }
        List<Projekt2ManagerDto> projekt2ManagerDtoList = projekt2ManagerService.findByManager(benutzer.getId()).stream().map(projekt2ManagerService::map).toList();
        PayloadTypeList<Projekt2ManagerDto> payloadTypeList = new PayloadTypeList<>(PayloadTypes.MANAGER_PROJEKT.getValue(), projekt2ManagerDtoList);
        return PayloadResponse.builder().success(true).data(List.of(payloadTypeList)).build();
    }

    @Override
    public ResponseEntity<PayloadResponse> cancelMoxisSigningRequest(String personalnummer, String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        if (!benutzerDetailsService.isUserEligible(token, Arrays.asList(FN_STAMMDATEN_ERFASSEN, FN_VERTRAGSDATEN_ERFASSEN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        WWorkflow contractWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.UNTERSCHRIFT_MOXIS_NEU_DIENSTVERTRAG);
        WWorkflow zusatzWorkflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer, SWorkflows.MA_VD_UNTERSCHRIFTENLAUF);
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        log.info("Sending request for cancel process to moxis-service for personalnummer {}", personalnummer);
        ResponseEntity<String> responseMoxis = moxisJobRestService.cancelJob(personalnummer, benutzer.getEmail());
        log.info("Received response for cancel process from moxis-service for personalnummer {}, with status {}, body {}", personalnummer, responseMoxis.getStatusCode(), responseMoxis.getBody());
        //setStammdatenInactive(personalnummer);
        //setVertragsdatenInactive(personalnummer);
        log.info("Set vertragsdaten and stammdaten inactive for personalnummer {}", personalnummer);
        PayloadResponse payloadResponse =  createPayloadResponseForMoxis(responseMoxis.getBody(), (zusatzWorkflow == null) ? contractWorkflow : zusatzWorkflow);
        if (!payloadResponse.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return checkResultIfNull(payloadResponse);
    }


    private PayloadResponse createPayloadResponseForMoxis(String moxisResponse, WWorkflow workflow) {
        PayloadResponse response = new PayloadResponse();
        PayloadTypeList<String> moxisPayloadType = createPayloadForMoxis(moxisResponse);
        PayloadTypeList<WorkflowDto> workflowDtoPayloadType = workflowHelperService.createPayloadForWorkflow(workflow);
        response.setSuccess(true);
        response.setData(Arrays.asList(moxisPayloadType, workflowDtoPayloadType));
        return response;
    }


    private PayloadTypeList<String> createPayloadForMoxis(String response) {
        PayloadTypeList<String> moxisPayloadType = new PayloadTypeList<>(PayloadTypes.MOXIS.getValue());
        moxisPayloadType.setAttributes(Collections.singletonList(response));
        return moxisPayloadType;
    }
}
