package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.persoenlicheSaetze.PersoenlicheSaetzeDataDto;
import com.ibosng.lhrservice.dtos.persoenlicheSaetze.PersoenlicheSaetzeDto;
import com.ibosng.lhrservice.dtos.persoenlicheSaetze.PersoenlicheSaetzeSingleTopLevelDto;
import com.ibosng.lhrservice.dtos.persoenlicheSaetze.PersoenlicheSaetzeTopLevelDto;
import com.ibosng.lhrservice.enums.PersoenlicheSatze;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.lhrservice.services.PersoenlicheSaetzeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.lhrservice.utils.Helpers.*;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToDouble;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersoenlicheSaetzeServiceImpl implements PersoenlicheSaetzeService {

    private final LHRClient lhrClient;
    private final HelperService helperService;
    private final LHREnvironmentService lhrEnvironmentService;
    private final VertragsdatenService vertragsdatenService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final GehaltInfoService gehaltinfoService;
    private final ArbeitszeitenService arbeitszeitenService;

    @Override
    public void processPersoenlicheSaetze(Personalnummer personalnummer, LocalDate date, PersoenlicheSatze persoenlicheSatze) throws LHRWebClientException {
        String firstDayOfMonth = getDateInPostRequestFormat(date.withDayOfMonth(1));
        PersoenlicheSaetzeSingleTopLevelDto singleTopLevelDto = getPersoenlicheSaetzeSingleDate(personalnummer, firstDayOfMonth, persoenlicheSatze.getSatzNr());

        if (isValidPersoenlicheSaetzeSingleDate(singleTopLevelDto)) {
            lhrClient.sendPersoenlicheSaetzeToLHR(null, mapPersoenlicheSaetzeSingleDateForLHR(personalnummer, persoenlicheSatze), persoenlicheSatze.getSatzNr(), firstDayOfMonth);
        } else {
            lhrClient.sendPersoenlicheSaetzeToLHR(mapPersoenlicheSaetzeForLHR(personalnummer, persoenlicheSatze), null, 1, null);
        }
    }

    private PersoenlicheSaetzeSingleTopLevelDto getPersoenlicheSaetzeSingleDate(Personalnummer personalnummer, String eintrittsDatum, Integer satzNr) {
        ResponseEntity<PersoenlicheSaetzeSingleTopLevelDto> response = (ResponseEntity<PersoenlicheSaetzeSingleTopLevelDto>) getPersoenlicheSaetze(personalnummer, eintrittsDatum, satzNr);
        return getBodyIfStatusOk(response);
    }

    public ResponseEntity<?> getPersoenlicheSaetze(Personalnummer personalnummer, String date, Integer satzNr) {
        return handleLHRExceptions(personalnummer.getPersonalnummer(), "variable daten", () ->
                lhrClient.getPersoenlicheSaetzeFromLHR(lhrEnvironmentService.getFaKz(personalnummer.getFirma()), lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), date, satzNr)
        );
    }

    private boolean isValidPersoenlicheSaetzeSingleDate(PersoenlicheSaetzeSingleTopLevelDto dto) {
        return Optional.ofNullable(dto)
                .map(PersoenlicheSaetzeSingleTopLevelDto::getPersoenlicherSatz)
                .map(daten -> daten.getData() != null)
                .orElse(false);
    }

    public PersoenlicheSaetzeSingleTopLevelDto mapPersoenlicheSaetzeSingleDateForLHR(Personalnummer personalnummer, PersoenlicheSatze persoenlicheSatze) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapPersoenlicheSaetzeSingleDateForLHR, stopping process!!!", new RuntimeException());
        }
        PersoenlicheSaetzeSingleTopLevelDto dto = new PersoenlicheSaetzeSingleTopLevelDto();
        dto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        PersoenlicheSaetzeDto persoenlicheSaetzeDto = new PersoenlicheSaetzeDto();
        if (vertragsdaten.getEintritt() != null) {
            persoenlicheSaetzeDto.setValidFrom(vertragsdaten.getEintritt().withDayOfMonth(1));
        }
        PersoenlicheSaetzeDataDto data = getPersoenlicheSaetzeDataForSatzNummer(vertragsdaten, persoenlicheSatze);
        if (data != null) {
            persoenlicheSaetzeDto.setData(data);
        }
        dto.setPersoenlicherSatz(persoenlicheSaetzeDto);
        return dto;
    }

    public PersoenlicheSaetzeTopLevelDto mapPersoenlicheSaetzeForLHR(Personalnummer personalnummer, PersoenlicheSatze persoenlicheSatze) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapPersoenlicheSaetzeForLHR, stopping process!!!", new RuntimeException());
        }
        PersoenlicheSaetzeTopLevelDto dto = new PersoenlicheSaetzeTopLevelDto();
        dto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        PersoenlicheSaetzeDto persoenlicheSaetzeDto = new PersoenlicheSaetzeDto();
        if (vertragsdaten.getEintritt() != null) {
            persoenlicheSaetzeDto.setValidFrom(vertragsdaten.getEintritt().withDayOfMonth(1));
        }
        PersoenlicheSaetzeDataDto data = getPersoenlicheSaetzeDataForSatzNummer(vertragsdaten, persoenlicheSatze);
        if (data != null) {
            persoenlicheSaetzeDto.setData(data);
        }
        dto.setPersoenlicheSaetze(List.of(persoenlicheSaetzeDto));
        return dto;
    }

    private PersoenlicheSaetzeDataDto getPersoenlicheSaetzeDataForSatzNummer(Vertragsdaten vertragsdaten, PersoenlicheSatze persoenlicheSatze) {
        PersoenlicheSaetzeDataDto data = null;
        if (persoenlicheSatze != null) {
            data = new PersoenlicheSaetzeDataDto();
            data.setSatznummer(persoenlicheSatze.getSatzNr());
            if (persoenlicheSatze.equals(PersoenlicheSatze.WOCHENSTUNDEN)) {
                setWochenstunden(data, vertragsdaten);
            }
            if (persoenlicheSatze.equals(PersoenlicheSatze.GEHALT)) {
                setGehalt(data, vertragsdaten);
            }
            if (persoenlicheSatze.equals(PersoenlicheSatze.ARBEITSTAGE)) {
                setArbeitstage(data, vertragsdaten);
            }
        }
        return data;
    }

    private void setWochenstunden(PersoenlicheSaetzeDataDto data, Vertragsdaten vertragsdaten) {
        data.setName("Wochenstunden");
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (arbeitszeitenInfo != null && arbeitszeitenInfo.getWochenstunden() != null) {
            data.setWert(parseStringToDouble(arbeitszeitenInfo.getWochenstunden()));
        }
    }

    private void setGehalt(PersoenlicheSaetzeDataDto data, Vertragsdaten vertragsdaten) {
        GehaltInfo gehaltInfo = gehaltinfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (gehaltInfo != null) {
            data.setWert(gehaltInfo.getGehaltVereinbart().doubleValue());
        }
    }

    private void setArbeitstage(PersoenlicheSaetzeDataDto data, Vertragsdaten vertragsdaten) {
        data.setName("Arbeitstage");
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (arbeitszeitenInfo != null) {
            List<Arbeitszeiten> arbeitszeitenList = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenInfo.getId()).stream().filter(az -> (az.getKernzeit() == null || Boolean.FALSE.equals(az.getKernzeit()))).toList();
            Arbeitszeiten arbeitszeiten = findFirstObject(arbeitszeitenList, new HashSet<>(List.of(String.valueOf(arbeitszeitenInfo.getId()))), "Arbeitszeiten");
            if (arbeitszeiten != null) {
                int arbeitstage = 0;
                if (arbeitszeiten.getMontagNetto() != null && arbeitszeiten.getMontagNetto() != 0.0) {
                    arbeitstage += 1;
                }
                if (arbeitszeiten.getDienstagNetto() != null && arbeitszeiten.getDienstagNetto() != 0.0) {
                    arbeitstage += 1;
                }
                if (arbeitszeiten.getMittwochNetto() != null && arbeitszeiten.getMittwochNetto() != 0.0) {
                    arbeitstage += 1;
                }
                if (arbeitszeiten.getDonnerstagNetto() != null && arbeitszeiten.getDonnerstagNetto() != 0.0) {
                    arbeitstage += 1;
                }
                if (arbeitszeiten.getFreitagNetto() != null && arbeitszeiten.getFreitagNetto() != 0.0) {
                    arbeitstage += 1;
                }
                if (arbeitszeiten.getSamstagNetto() != null && arbeitszeiten.getSamstagNetto() != 0.0) {
                    arbeitstage += 1;
                }
                if (arbeitszeiten.getSonntagNetto() != null && arbeitszeiten.getSonntagNetto() != 0.0) {
                    arbeitstage += 1;
                }
                data.setWert(arbeitstage);
            }
        }
    }

    @Override
    public boolean shouldSendGehalt(Integer vertragsdatenId) {
        GehaltInfo gehaltInfo = gehaltinfoService.findByVertragsdatenId(vertragsdatenId);
        if (gehaltInfo != null && gehaltInfo.getGehaltVereinbart() != null && gehaltInfo.getKvGehaltBerechnet() != null) {
            // gehaltInfo.getGehaltVereinbart() is greater than gehaltInfo.getKvGehaltBerechnet()
            return gehaltInfo.getGehaltVereinbart().compareTo(gehaltInfo.getKvGehaltBerechnet()) > 0;
        }
        return false;
    }
}
