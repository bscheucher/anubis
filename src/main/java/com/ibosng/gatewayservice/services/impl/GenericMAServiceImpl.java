package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.telefon.Telefon;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.UnterhaltsberechtigteService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VordienstzeitenService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.Gateway2Validation;
import com.ibosng.gatewayservice.services.GenericMAService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.gatewayservice.utils.Constants.GATEWAY_SERVICE;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericMAServiceImpl implements GenericMAService {
    private final ManageWFsService manageWFsService;
    private final WWorkflowItemService wWorkflowItemService;
    private final ManageWFItemsService manageWFItemsService;
    private final Gateway2Validation gateway2Validation;
    private final VordienstzeitenService vordienstzeitenService;
    private final UnterhaltsberechtigteService unterhaltsberechtigteService;
    private final VertragsdatenService vertragsdatenService;
    private final StammdatenService stammdatenService;
    private final TeilnehmerService teilnehmerService;

    @Override
    public boolean isVertragsdatenWithoutError(Vertragsdaten vertragsdaten) {
        if (vertragsdaten == null) {
            log.error("Vertragsdaten are null!");
            return false;
        }
        if (!vertragsdaten.getErrors().isEmpty()) {
            return false;
        }
        List<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findAllByVertragsdatenId(vertragsdaten.getId());
        for (Vordienstzeiten vordienstzeit : vordienstzeiten) {
            if (!vordienstzeit.getErrors().isEmpty()) {
                return false;
            }
        }
        List<Unterhaltsberechtigte> unterhaltsberechtigte = unterhaltsberechtigteService.findAllByVertragsdatenId(vertragsdaten.getId());
        for (Unterhaltsberechtigte unterhaltsberechtigt : unterhaltsberechtigte) {
            if (!unterhaltsberechtigt.getErrors().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void manageKVCalculation(String personalnummer, WWorkflow workflow, String changedBy, boolean isOnboarding) {
        WWorkflowStatus wwiKVStatus = gateway2Validation.calculateKVEinstufung(personalnummer, changedBy, isOnboarding);
        if (isOnboarding) {
            WWorkflowItem wwiDvEinstufung = wWorkflowItemService.findByWorkflowAndWorkflowItemName(workflow, SWorkflowItems.KV_EINSTUFUNG_BERECHNEN.getValue());
            wWorkflowItemService.refreshWorkflowItem(wwiDvEinstufung);
            if (Objects.equals(wwiKVStatus, WWorkflowStatus.COMPLETED) && wwiDvEinstufung.getStatus().equals(WWorkflowStatus.COMPLETED)) {
                manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN, WWorkflowStatus.COMPLETED, changedBy);
                WWorkflow wwLohnverrechnung = manageWFsService.setWFStatus(workflow.getWorkflowGroup(), SWorkflows.LOHNVERRECHNUNG, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
                manageWFItemsService.setWFItemStatus(wwLohnverrechnung, SWorkflowItems.LOHNVERRECHUNG_INFORMIEREN, WWorkflowStatus.IN_PROGRESS, GATEWAY_SERVICE);
            }
        }
    }

    @Override
    public PayloadTypeList<VordienstzeitenDto> createPayloadForMultipleVordienstzeiten(Vertragsdaten vertragsdaten) {
        PayloadTypeList<VordienstzeitenDto> vordienstzeitenDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VORDIENSTZEITEN.getValue());
        // Null check to account for personalnummers belonging to Teilnehmer
        if (vertragsdaten != null) {
            List<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findAllByVertragsdatenId(vertragsdaten.getId());
            List<VordienstzeitenDto> vordienstzeitenDtos = vordienstzeiten.stream().map(vordienstzeitenService::mapVordienstzeitenToDto).collect(Collectors.toList());
            vordienstzeitenDtoPayloadType.setAttributes(vordienstzeitenDtos);
        } else {
            vordienstzeitenDtoPayloadType.setAttributes(new ArrayList<>());
        }
        return vordienstzeitenDtoPayloadType;
    }

    @Override
    public PayloadTypeList<UnterhaltsberechtigteDto> createPayloadForMultipleUnterhaltsberechtigte(Vertragsdaten vertragsdaten) {
        PayloadTypeList<UnterhaltsberechtigteDto> unterhaltsberechtigteDtoPayloadType = new PayloadTypeList<>(PayloadTypes.UNTERHALTSBERECHTIGTE.getValue());
        // Null check to account for personalnummers belonging to Teilnehmer
        if (vertragsdaten != null) {
            List<Unterhaltsberechtigte> unterhaltsberechtigteList = unterhaltsberechtigteService.findAllByVertragsdatenId(vertragsdaten.getId());
            List<UnterhaltsberechtigteDto> unterhaltsberechtigteDtos = unterhaltsberechtigteList.stream().map(unterhaltsberechtigteService::mapUnterhaltsberechtigteToDto).collect(Collectors.toList());
            unterhaltsberechtigteDtoPayloadType.setAttributes(unterhaltsberechtigteDtos);
        } else {
            unterhaltsberechtigteDtoPayloadType.setAttributes(new ArrayList<>());
        }
        return unterhaltsberechtigteDtoPayloadType;
    }

    @Override
    public PayloadTypeList<VordienstzeitenDto> createPayloadForSingleVordienstzeiten(VordienstzeitenDto vordienstzeitenDto) {
        PayloadTypeList<VordienstzeitenDto> vordienstzeitenDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VORDIENSTZEIT.getValue());
        vordienstzeitenDtoPayloadType.setAttributes(Collections.singletonList(vordienstzeitenDto));
        return vordienstzeitenDtoPayloadType;
    }

    @Override
    public PayloadTypeList<UnterhaltsberechtigteDto> createPayloadForSingleUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto) {
        PayloadTypeList<UnterhaltsberechtigteDto> unterhaltsberechtigteDtoPayloadType = new PayloadTypeList<>(PayloadTypes.UNTERHALTSBERECHTIGT.getValue());
        unterhaltsberechtigteDtoPayloadType.setAttributes(Collections.singletonList(unterhaltsberechtigteDto));
        return unterhaltsberechtigteDtoPayloadType;
    }

    @Override
    public Vertragsdaten getVertragsdatenEntity(String personalnummer, Boolean isOnboarding) {
        Vertragsdaten vertragsdaten;
        if (isOnboarding) {
            vertragsdaten = vertragsdatenService.findAllByPNAndStatusesNotInVertragsdatenaenderungen(personalnummer, List.of(MitarbeiterStatus.NEW, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED)).stream().findFirst().orElse(null);
        } else {
            vertragsdaten = vertragsdatenService.findAllByPNAndStatusesNotInVertragsdatenaenderungen(personalnummer, List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED)).stream().findFirst().orElse(null);
        }
        return vertragsdaten;
    }

    @Override
    public PayloadTypeList<StammdatenDto> createPayloadForStammdaten(StammdatenDto stammdatenDto) {
        PayloadTypeList<StammdatenDto> stammDatenPayloadType = new PayloadTypeList<>(PayloadTypes.STAMMDATEN.getValue());
        stammDatenPayloadType.setAttributes(Collections.singletonList(stammdatenDto));
        return stammDatenPayloadType;
    }

    @Override
    public WWorkflow getWorkflowForMaType(Personalnummer personalnummer) {
        WWorkflow workflow = null;
        if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.MITARBEITER)) {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer.getPersonalnummer(), SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        } else if (personalnummer.getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
            workflow = manageWFsService.getWorkflowFromDataAndWFType(personalnummer.getPersonalnummer(), SWorkflows.TN_ONBOARDING_ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        }
        return workflow;
    }

    @Override
    public PayloadTypeList<MitarbeiterType> createPayloadForMAType(MitarbeiterType mitarbeiterType) {
        PayloadTypeList<MitarbeiterType> maTypePayloadType = new PayloadTypeList<>(PayloadTypes.MITARBEITER_TYPE.getValue());
        maTypePayloadType.setAttributes(Collections.singletonList(mitarbeiterType));
        return maTypePayloadType;
    }

    @Override
    public PayloadTypeList<VertragsdatenDto> createPayloadForVertragsdaten(VertragsdatenDto vertragsDatenDto, boolean isMitarbeiter) {
        if (!isMitarbeiter && isNullOrBlank(vertragsDatenDto.getWochenstunden())) {
            vertragsDatenDto.setWochenstunden("38.5");
        }
        PayloadTypeList<VertragsdatenDto> vertragsDatenDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VERTRAGSDATEN.getValue());
        vertragsDatenDtoPayloadType.setAttributes(Collections.singletonList(vertragsDatenDto));
        return vertragsDatenDtoPayloadType;
    }

    @Override
    public void updateTeilnehmerStammdaten(Stammdaten stammdaten, String changedBy) {
        Teilnehmer teilnehmer = teilnehmerService.findByPersonalnummerString(stammdaten.getPersonalnummer().getPersonalnummer());

        if (teilnehmer != null) {
            teilnehmer.setVorname(stammdaten.getVorname());
            teilnehmer.setNachname(stammdaten.getNachname());
            teilnehmer.setGeburtsdatum(stammdaten.getGeburtsdatum());
            teilnehmer.setEmail(stammdaten.getEmail());
            teilnehmer.setGeschlecht(stammdaten.getGeschlecht());
            teilnehmer.setSvNummer(stammdaten.getSvnr());
            //TODO Update adresse
            teilnehmer.setAdresse(stammdaten.getAdresse());
            teilnehmer.setAnrede(stammdaten.getAnrede());
            teilnehmer.setTitel(stammdaten.getTitel() != null ? stammdaten.getTitel().getName() : null);

            if (stammdaten.getMobilnummer() != null) {
                if (!teilnehmer.getTelefons().isEmpty()) {
                    updateTelefon(teilnehmer.getTelefons().get(0), stammdaten.getMobilnummer());
                }

            }
            teilnehmer.setMuttersprache(stammdaten.getMuttersprache());
            if (stammdaten.getStaatsbuergerschaft() != null) {
                Set<Land> mutableSet = new HashSet<>();
                mutableSet.add(stammdaten.getStaatsbuergerschaft());
                teilnehmer.setNation(mutableSet);
            }
            teilnehmer.setChangedBy(changedBy);
            TeilnehmerDto teilnehmerDto = new TeilnehmerDto();
            teilnehmerDto.setId(teilnehmer.getId());
            teilnehmerService.save(teilnehmer);
        }
    }

    public void updateTelefon(Telefon current, Telefon other) {
        current.setStatus(other.getStatus());
        current.setTelefonnummer(other.getTelefonnummer());
        current.setOwner(other.getOwner());
        current.setLand(other.getLand());
        current.setCreatedOn(other.getCreatedOn());
        current.setCreatedBy(other.getCreatedBy());
        current.setChangedBy(other.getChangedBy());
    }

    @Override
    public boolean isVertragsdatenWithoutError(String personalnummer, boolean isOnboarding) {
        Vertragsdaten vertragsdaten = getVertragsdatenEntity(personalnummer, isOnboarding);
        if (vertragsdaten == null) {
            log.error("Vertragsdaten for personalnummer {} are null!", personalnummer);
            return false;
        }
        if (!vertragsdaten.getErrors().isEmpty()) {
            return false;
        }
        List<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findAllByVertragsdatenId(vertragsdaten.getId());
        for (Vordienstzeiten vordienstzeit : vordienstzeiten) {
            if (!vordienstzeit.getErrors().isEmpty()) {
                return false;
            }
        }
        List<Unterhaltsberechtigte> unterhaltsberechtigte = unterhaltsberechtigteService.findAllByVertragsdatenId(vertragsdaten.getId());
        for (Unterhaltsberechtigte unterhaltsberechtigt : unterhaltsberechtigte) {
            if (!unterhaltsberechtigt.getErrors().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Stammdaten getStammdatenEntity(String personalnummer, Boolean isOnboarding) {
        Stammdaten stammdaten;
        if (isOnboarding) {
            stammdaten = stammdatenService.findByPersonalnummerStringAndStatusIn(personalnummer, List.of(MitarbeiterStatus.NEW, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED));
        } else {
            stammdaten = stammdatenService.findByPersonalnummerStringAndStatusIn(personalnummer, List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED));
        }
        return stammdaten;
    }

    @Override
    public boolean isStammdatenWithoutError(String personalnummer, boolean isOnboarding) {
        Stammdaten stammdaten = getStammdatenEntity(personalnummer, isOnboarding);
        if (stammdaten == null) {
            log.error("Stammdaten for personalnummer {} are null!", personalnummer);
            return false;
        }
        if (!stammdaten.getErrors().isEmpty() || !MitarbeiterStatus.ACTIVE.equals(getStammdatenEntity(personalnummer, false).getStatus())) {
            return false;
        }
        return true;
    }

    @Override
    public PayloadTypeList<VordienstzeitenDto> createPayloadForMultipleVordienstzeiten(String personalnummer, boolean isOnboarding) {
        PayloadTypeList<VordienstzeitenDto> vordienstzeitenDtoPayloadType = new PayloadTypeList<>(PayloadTypes.VORDIENSTZEITEN.getValue());
        Vertragsdaten vertragsdaten = getVertragsdatenEntity(personalnummer, isOnboarding);
        // Null check to account for personalnummers belonging to Teilnehmer
        if (vertragsdaten != null) {
            List<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findAllByVertragsdatenId(vertragsdaten.getId());
            List<VordienstzeitenDto> vordienstzeitenDtos = vordienstzeiten.stream().map(vordienstzeitenService::mapVordienstzeitenToDto).collect(Collectors.toList());
            vordienstzeitenDtoPayloadType.setAttributes(vordienstzeitenDtos);
        } else {
            vordienstzeitenDtoPayloadType.setAttributes(new ArrayList<>());
        }
        return vordienstzeitenDtoPayloadType;
    }

    @Override
    public PayloadTypeList<UnterhaltsberechtigteDto> createPayloadForMultipleUnterhaltsberechtigte(String personalnummer, boolean isOnboarding) {
        PayloadTypeList<UnterhaltsberechtigteDto> unterhaltsberechtigteDtoPayloadType = new PayloadTypeList<>(PayloadTypes.UNTERHALTSBERECHTIGTE.getValue());
        Vertragsdaten vertragsdaten = getVertragsdatenEntity(personalnummer, isOnboarding);
        // Null check to account for personalnummers belonging to Teilnehmer
        if (vertragsdaten != null) {
            List<Unterhaltsberechtigte> unterhaltsberechtigteList = unterhaltsberechtigteService.findAllByVertragsdatenId(vertragsdaten.getId());
            List<UnterhaltsberechtigteDto> unterhaltsberechtigteDtos = unterhaltsberechtigteList.stream().map(unterhaltsberechtigteService::mapUnterhaltsberechtigteToDto).collect(Collectors.toList());
            unterhaltsberechtigteDtoPayloadType.setAttributes(unterhaltsberechtigteDtos);
        } else {
            unterhaltsberechtigteDtoPayloadType.setAttributes(new ArrayList<>());
        }
        return unterhaltsberechtigteDtoPayloadType;
    }
}
