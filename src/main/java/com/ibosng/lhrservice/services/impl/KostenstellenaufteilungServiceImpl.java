package com.ibosng.lhrservice.services.impl;

import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.ProjektType;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.services.ProjektTypeService;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import com.ibosng.lhrservice.dtos.GruppeNameNrDto;
import com.ibosng.lhrservice.dtos.kostenstellenaufteilung.KostenstellenaufteilungDataDto;
import com.ibosng.lhrservice.dtos.kostenstellenaufteilung.KostenstellenaufteilungDto;
import com.ibosng.lhrservice.dtos.kostenstellenaufteilung.KostenstellenaufteilungSingleTopLevelDto;
import com.ibosng.lhrservice.dtos.kostenstellenaufteilung.KostenstellenaufteilungTopLevelDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.KostenstellenaufteilungService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.lhrservice.utils.Helpers.*;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class KostenstellenaufteilungServiceImpl implements KostenstellenaufteilungService {
    private final LHRClient lhrClient;
    private final HelperService helperService;
    private final LHREnvironmentService lhrEnvironmentService;
    private final VertragsdatenService vertragsdatenService;
    private final PersonalnummerService personalnummerService;
    private final TeilnehmerService teilnehmerService;
    private final ProjektTypeService projektTypeService;


    @Transactional(transactionManager = "postgresTransactionManager", readOnly = true)
    @Override
    public void processKostenstellenaufteilung(Personalnummer personalnummer, String eintrittsdatum) throws LHRWebClientException {
        KostenstellenaufteilungTopLevelDto existingData = getKostenstellenaufteiluing(personalnummer);

        if (existingData != null && !existingData.getKostenstellenaufteilung().isEmpty()) {
            for (KostenstellenaufteilungDto dto : existingData.getKostenstellenaufteilung()) {
                if (!isNullOrBlank(eintrittsdatum) && dto.getValidFrom() != null && parseDate(eintrittsdatum).getMonth().equals(dto.getValidFrom().getMonth())) {
                    log.error("There already exists a kostenstellenaufteilung for a different day in the same month for personalnummer {}.", personalnummer);
                    if (!lhrEnvironmentService.isProduction()) {
                        lhrClient.deleteKostenstellenaufteilungFromLHR(
                                lhrEnvironmentService.getFaKz(personalnummer.getFirma()),
                                lhrEnvironmentService.getFaNr(personalnummer.getFirma()),
                                parseStringToInteger(personalnummer.getPersonalnummer()),
                                getDateInPostRequestFormat(dto.getValidFrom()));
                    } else {
                        throw new LHRException(String.format("There already exists a kostenstellenaufteilung for a different day in the same month for personalnummer %s.", personalnummer));
                    }
                }
            }
        }

        KostenstellenaufteilungSingleTopLevelDto datenSingleDate = getKostenstellenaufteiluingSingleDate(personalnummer, eintrittsdatum);
        if (isValidKostenstellenaufteilungSingleDate(datenSingleDate)) {
            lhrClient.sendKostenstellenaufteilungToLHR(null, mapKostenstellenaufteilungSingleDateForLHR(personalnummer, datenSingleDate), eintrittsdatum);
        } else {
            lhrClient.sendKostenstellenaufteilungToLHR(mapKostenstellenaufteilungForLHR(personalnummer), null, null);
        }
    }


    private KostenstellenaufteilungSingleTopLevelDto getKostenstellenaufteiluingSingleDate(Personalnummer personalnummer, String date) {
        ResponseEntity<KostenstellenaufteilungSingleTopLevelDto> response = (ResponseEntity<KostenstellenaufteilungSingleTopLevelDto>) getKostenstellenaufteilingDaten(personalnummer, date);
        return getBodyIfStatusOk(response);
    }

    private KostenstellenaufteilungTopLevelDto getKostenstellenaufteiluing(Personalnummer personalnummer) {
        ResponseEntity<KostenstellenaufteilungTopLevelDto> response = (ResponseEntity<KostenstellenaufteilungTopLevelDto>) getKostenstellenaufteilingDaten(personalnummer, null);
        return getBodyIfStatusOk(response);
    }

    private boolean isValidKostenstellenaufteilungSingleDate(KostenstellenaufteilungSingleTopLevelDto dto) {
        return Optional.ofNullable(dto)
                .map(KostenstellenaufteilungSingleTopLevelDto::getKostenstellenaufteilung)
                .map(daten -> daten.getData() != null)
                .orElse(false);
    }

    private ResponseEntity<?> getKostenstellenaufteilingDaten(Personalnummer personalnummer, String date) {
        if (!isNullOrBlank(date)) {
            return handleLHRExceptions(personalnummer.getPersonalnummer(), "kostenstellenaufteilung", () ->
                    lhrClient.getKostenstellenaufteilungFromLHR(lhrEnvironmentService.getFaKz(personalnummer.getFirma()), lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), date)
            );
        }
        return handleLHRExceptions(personalnummer.getPersonalnummer(), "kostenstellenaufteilung", () ->
                lhrClient.getKostenstellenaufteilungFromLHR(lhrEnvironmentService.getFaKz(personalnummer.getFirma()), lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), null)
        );
    }

    private KostenstellenaufteilungSingleTopLevelDto mapKostenstellenaufteilungSingleDateForLHR(
            Personalnummer personalnummer,
            KostenstellenaufteilungSingleTopLevelDto existingDatenSingleDate) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapKostenstellenaufteilungSingleDateForLHR, stopping process!!!", new RuntimeException());
        }
        KostenstellenaufteilungSingleTopLevelDto dto = new KostenstellenaufteilungSingleTopLevelDto();
        dto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        if (existingDatenSingleDate != null) {
            dto.setKostenstellenaufteilung(getKostenstellenaufteilung(existingDatenSingleDate.getKostenstellenaufteilung(), vertragsdaten));
        } else {
            dto.setKostenstellenaufteilung(getKostenstellenaufteilung(null, vertragsdaten));
        }
        return dto;
    }

    private KostenstellenaufteilungTopLevelDto mapKostenstellenaufteilungForLHR(Personalnummer personalnummer) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerIdAndStatus(personalnummer.getId(), List.of(MitarbeiterStatus.VALIDATED))
                .stream().findFirst().orElse(null);
        if (vertragsdaten == null) {
            throw new LHRException("Vertragsdaten are null in mapKostenstellenaufteilungForLHR, stopping process!!!", new RuntimeException());
        }
        KostenstellenaufteilungTopLevelDto dto = new KostenstellenaufteilungTopLevelDto();
        dto.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
        dto.setKostenstellenaufteilung(List.of(getKostenstellenaufteilung(null, vertragsdaten)));
        return dto;
    }

    private KostenstellenaufteilungDto getKostenstellenaufteilung(KostenstellenaufteilungDto existingData, Vertragsdaten vertragsdaten) {
        if (existingData == null) {
            existingData = new KostenstellenaufteilungDto();
        }
        existingData.setData(new ArrayList<>());
        List<KostenstellenaufteilungDataDto> data = existingData.getData();
        if (vertragsdaten.getEintritt() != null) {
            existingData.setValidFrom(vertragsdaten.getEintritt());
        }
        if (vertragsdaten.getKostenstelle() != null) {
            KostenstellenaufteilungDataDto kostenstellenaufteilungDataDto = new KostenstellenaufteilungDataDto();
            GruppeNameNrDto kostenstelle = new GruppeNameNrDto();
            kostenstelle.setName(vertragsdaten.getKostenstelle().getBezeichnung());
            kostenstelle.setNr(vertragsdaten.getKostenstelle().getNummer());
            kostenstellenaufteilungDataDto.setKostenstelle(kostenstelle);
            GruppeNameKzDto konstentraeger = new GruppeNameKzDto();
            if (MitarbeiterType.MITARBEITER.equals(vertragsdaten.getPersonalnummer().getMitarbeiterType())) {
                konstentraeger.setKz("0");
            } else {
                Teilnehmer teilnehmer = teilnehmerService.findByPersonalnummerId(vertragsdaten.getPersonalnummer().getId());
                if (teilnehmer != null) {
                    List<ProjektType> projektTypesUeba = projektTypeService.findAll().stream().filter(pr -> pr.getName().contains("ÜBA")).toList();
                    LocalDate localDate = LocalDate.now();
                    List<Projekt> projekts = teilnehmer.getSeminars()
                            .stream()
                            .map(Seminar::getProject)
                            .filter(pr -> projektTypesUeba.contains(pr.getProjektType()) && (pr.getEndDate().isAfter(localDate) && (vertragsdaten.getEintritt().isAfter(pr.getStartDate()) || vertragsdaten.getEintritt().equals(pr.getStartDate())))).toList();
                    if (projekts.size() > 1) {
                        log.warn("More than 1 active UEBA project found for teilnehmer with personalnummer {}", vertragsdaten.getPersonalnummer().getPersonalnummer());
                    } else if (projekts.isEmpty()) {
                        log.warn("No active UEBA project found for teilnehmer with personalnummer {}", vertragsdaten.getPersonalnummer().getPersonalnummer());

                    } else {
                        Projekt projekt = projekts.get(0);
                        String formattedKostenstelleGruppe = String.format("%02d", projekt.getKostenstelleGruppe());
                        String formattedKostenstelle = String.format("%02d", projekt.getKostenstelle());
                        String formattedKostentraeger = String.format("%03d", projekt.getKostentraeger());
                        String kostentraeger = formattedKostenstelleGruppe + formattedKostenstelle + formattedKostentraeger;
                        if (!isNullOrBlank(kostentraeger)) {
                            konstentraeger.setKz(kostentraeger);
                        }
                    }
                }
            }

            kostenstellenaufteilungDataDto.setKostentraeger(konstentraeger);
            kostenstellenaufteilungDataDto.setAnteil(100);
            kostenstellenaufteilungDataDto.setStammKostenstelle(true);
            data.add(kostenstellenaufteilungDataDto);
        }

        return existingData;
    }

}
